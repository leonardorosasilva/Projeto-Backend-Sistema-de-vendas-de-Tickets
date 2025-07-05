package com.example.ticketmaster.config;

import com.example.ticketmaster.security.CustomUserDetailsService;
import com.example.ticketmaster.security.JwtAuthEntryPoint;
import com.example.ticketmaster.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.beans.factory.annotation.Value;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Permite usar @PreAuthorize em métodos de serviços/controladores
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthEntryPoint authenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.cors.allowed-origins}")
    private String[] allowedOrigins; // Para ler as origens do application.properties

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtAuthEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) // Desabilita CSRF para APIs REST
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)) // Define nosso entry point para erros de autenticação
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT é stateless, não usa sessão
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público aos endpoints de autenticação e documentação
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Permite acesso público de leitura para eventos e locais
                        .requestMatchers(HttpMethod.GET, "/api/events/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/venues/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tickets/**").permitAll() // Pode permitir ver tipos de ingressos por evento
                        // Restringe acesso de escrita/deleção
                        .requestMatchers(HttpMethod.POST, "/api/events").hasRole("ORGANIZER") // Apenas organizadores podem criar eventos
                        .requestMatchers(HttpMethod.PUT, "/api/events/**").hasRole("ORGANIZER")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/**").hasRole("ORGANIZER")
                        .requestMatchers(HttpMethod.POST, "/api/venues").hasRole("ADMIN") // Apenas admins podem criar locais
                        .requestMatchers(HttpMethod.PUT, "/api/venues/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/venues/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/tickets").hasRole("ORGANIZER") // Organizadores criam tipos de ingresso
                        .requestMatchers(HttpMethod.PUT, "/api/tickets/**").hasRole("ORGANIZER")
                        .requestMatchers(HttpMethod.DELETE, "/api/tickets/**").hasRole("ORGANIZER")
                        .requestMatchers("/api/purchases/**").hasRole("USER") // Apenas usuários podem fazer/ver compras
                        .requestMatchers("/api/users/**").hasRole("ADMIN") // Acesso a usuários por ADMIN
                        // Qualquer outra requisição exige autenticação
                        .anyRequest().authenticated()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Adiciona nosso filtro JWT

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // Usar allowedOrigins do application.properties
        for (String origin : allowedOrigins) {
            config.addAllowedOrigin(origin.trim());
        }
        config.addAllowedHeader("*"); // Permite todos os cabeçalhos
        config.addAllowedMethod("*"); // Permite todos os métodos (GET, POST, PUT, DELETE, etc.)
        source.registerCorsConfiguration("/**", config); // Aplica a configuração a todas as rotas
        return new CorsFilter(source);
    }
}
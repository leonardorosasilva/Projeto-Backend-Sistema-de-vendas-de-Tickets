🎫 TicketMaster Clone - Backend Spring Boot

Este é o backend de uma aplicação de venda de ingressos estilo Ticketmaster, construído com Spring Boot. Ele gerencia eventos, locais, tipos de ingressos, autenticação de usuários (compradores, organizadores, administradores) e o fluxo de compra de ingressos, incluindo o controle de estoque e resgate.

🚀 Tecnologias Utilizadas

    Spring Boot 3.3.1: Framework para desenvolvimento rápido de aplicações Java.

    Spring Data JPA / Hibernate: Para persistência e acesso a dados com banco de dados relacional.

    Spring Security: Para autenticação e autorização, protegendo os endpoints da API.

    JWT (JSON Web Tokens): Para autenticação sem estado e segura da API.

    PostgreSQL: Banco de dados relacional (configurável para MySQL, H2, etc.).

    Lombok: Reduz o código boilerplate (getters, setters, construtores).

    ModelMapper: Para mapeamento entre Entidades e DTOs.

    Springdoc OpenAPI (Swagger UI): Para documentação interativa da API.

    Maven: Gerenciador de dependências e build do projeto.

    Java 17: Linguagem de programação.

✨ Funcionalidades Principais

    Autenticação e Autorização:

        Registro e Login de usuários.

        Roles de usuário: USER (comprador), ORGANIZER (organizador de eventos), ADMIN (administrador do sistema).

        Proteção de rotas baseada em roles usando JWT.

    Gerenciamento de Usuários:

        Cadastro e listagem de usuários (acesso restrito ao ADMIN).

        Visualização e edição do próprio perfil de usuário.

    Gerenciamento de Locais (Venues):

        CRUD (Criar, Ler, Atualizar, Deletar) de locais de eventos.

        Acesso restrito ao ADMIN.

    Gerenciamento de Eventos:

        CRUD de eventos (nome, descrição, data/hora, local, organizador, categoria).

        Busca de eventos por nome ou categoria.

        Ativação/desativação de eventos.

        Acesso restrito para criação/atualização/deleção (ORGANIZER e ADMIN).

    Gerenciamento de Ingressos:

        CRUD de tipos de ingressos por evento (preço, quantidade, tipo, setor).

        Controle de quantidade disponível.

        Acesso restrito para criação/atualização/deleção (ORGANIZER e ADMIN).

    Fluxo de Compra:

        Criação de compras, gerenciando múltiplos tipos de ingressos.

        Atualização automática do estoque de ingressos.

        Status da compra (PENDING, COMPLETED, CANCELLED).

        Geração de código único para cada ingresso comprado (simulando QR code).

    Resgate de Ingressos:

        Endpoint para marcar um ingresso como "resgatado" (usado no evento) via seu código único.

        Acesso restrito ao ORGANIZER e ADMIN.

    Tratamento de Exceções Global: Respostas de erro padronizadas (JSON) para validações e erros de negócio.

    CORS Configuration: Suporte a requisições de diferentes origens para integração com frontend.

🛠️ Pré-requisitos

Antes de executar a aplicação, certifique-se de ter instalado:

    Java Development Kit (JDK) 17 ou superior.

    Apache Maven 3.6 ou superior.

    PostgreSQL (ou o banco de dados de sua preferência) instalado e rodando.

⚙️ Configuração do Ambiente

    Clone o Repositório:
    Bash

git clone https://github.com/seu-usuario/ticketmaster-clone.git
cd ticketmaster-clone

Configuração do Banco de Dados:

    Crie um banco de dados PostgreSQL com o nome ticketmaster_db.

    Certifique-se de ter um usuário e senha de banco de dados configurados.

    Edite o arquivo src/main/resources/application.properties com as suas credenciais:

Properties

# Database Configuration (PostgreSQL Example)
spring.datasource.url=jdbc:postgresql://localhost:5432/ticketmaster_db
spring.datasource.username=seu_usuario_db
spring.datasource.password=sua_senha_db
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update # Para desenvolvimento, ele cria/atualiza as tabelas. Mude para 'none' em produção.

Configurações de Segurança (JWT e CORS):

    No mesmo arquivo application.properties, configure sua chave secreta JWT (mude para uma string longa e aleatória em produção!) e as origens permitidas para CORS (geralmente, a URL do seu frontend).

Properties

# JWT Configuration
jwt.secret=SuaChaveSecretaMuitoLongaESeguraParaJWTQueDeveTerPeloMenos256Bits
jwt.expiration.ms=86400000 # 24 horas em milissegundos

# CORS Configuration
app.cors.allowed-origins=http://localhost:3000,http://localhost:4200,http://localhost:8080

Popular Roles Iniciais (Opcional, mas Recomendado):
Para que os usuários possam ser registrados com roles, as roles ROLE_USER, ROLE_ORGANIZER e ROLE_ADMIN precisam existir na tabela roles do seu banco de dados. Você pode fazer isso manualmente com SQL:
SQL

    INSERT INTO roles (name) VALUES ('ROLE_USER');
    INSERT INTO roles (name) VALUES ('ROLE_ORGANIZER');
    INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

    Ou crie um CommandLineRunner no Spring Boot para isso.

🏃 Como Executar

    Construa o Projeto:
    Bash

mvn clean install

Execute a Aplicação:
Bash

    mvn spring-boot:run

    A aplicação estará rodando em http://localhost:8080 (ou na porta configurada no application.properties).

📚 Documentação da API (Swagger UI)

Após iniciar a aplicação, você pode acessar a documentação interativa da API em:
👉 http://localhost:8080/swagger-ui.html

Aqui você poderá ver todos os endpoints disponíveis, seus métodos HTTP, parâmetros de requisição, modelos de resposta e até mesmo testá-los diretamente.

🧪 Testando a API

Você pode usar ferramentas como Postman, Insomnia ou curl para interagir com a API.

Exemplo de Fluxo:

    Registrar um Usuário (ROLE_USER padrão):
    POST /api/auth/register
    Corpo:
    JSON

{
  "username": "comprador1",
  "email": "comprador1@email.com",
  "password": "senha123"
}

    A resposta incluirá um token JWT. Salve-o.

Fazer Login para Obter um Novo Token (se o anterior expirar ou para outro usuário):
POST /api/auth/login
Corpo:
JSON

{
  "usernameOrEmail": "comprador1",
  "password": "senha123"
}

    A resposta incluirá um token JWT.

Acessar um Endpoint Protegido (ex: listar eventos como usuário logado):
GET /api/events
Cabeçalho: Authorization: Bearer <SEU_TOKEN_JWT>

Criar um Local (requer ROLE_ADMIN):

    Primeiro, registre ou altere a role de um usuário para ADMIN (manual ou via um endpoint de admin, se implementado).

    Faça login como ADMIN para obter o token.

    POST /api/venues
    Cabeçalho: Authorization: Bearer <TOKEN_ADMIN>
    Corpo:
    JSON

    {
      "name": "Arena de Eventos",
      "address": "Rua Principal, 123",
      "city": "Minha Cidade",
      "state": "SC",
      "zipCode": "89200-000",
      "capacity": 15000
    }

Criar um Evento (requer ROLE_ORGANIZER ou ADMIN):

    Certifique-se de ter um venueId e um organizerId (ID de um usuário com role ORGANIZER ou ADMIN).

    POST /api/events
    Cabeçalho: Authorization: Bearer <TOKEN_ORGANIZER_OU_ADMIN>
    Corpo:
    JSON

    {
      "name": "Show da Banda XYZ",
      "description": "Grande show com a banda XYZ e convidados.",
      "dateTime": "2025-12-25T20:00:00",
      "venueId": 1,
      "organizerId": 2,
      "category": "Música"
    }

Criar Tipos de Ingressos para o Evento:

    POST /api/tickets
    Cabeçalho: Authorization: Bearer <TOKEN_ORGANIZER_OU_ADMIN>
    Corpo:
    JSON

    {
      "eventId": 1,
      "type": "Pista",
      "price": 100.50,
      "totalQuantity": 5000,
      "section": "Pista Comum"
    }

Realizar uma Compra (requer ROLE_USER):

    Faça login como um USER para obter o token.

    POST /api/purchases
    Cabeçalho: Authorization: Bearer <TOKEN_USER>
    Corpo:
    JSON

        {
          "userId": 1,
          "purchasedTickets": [
            {
              "ticketId": 1,
              "quantity": 2
            }
          ]
        }

Lembre-se de substituir IDs, tokens e dados conforme o seu ambiente de teste!

🤝 Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues para bugs, sugestões ou enviar pull requests.

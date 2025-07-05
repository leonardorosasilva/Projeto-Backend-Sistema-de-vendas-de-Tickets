package com.example.ticketmaster.services;

import com.example.ticketmaster.dto.user.UserResponseDTO;
import com.example.ticketmaster.exception.ResourceNotFoundException; // Criaremos esta exceção depois
import com.example.ticketmaster.model.User;
import com.example.ticketmaster.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    // Exemplo: Método para atualizar informações do usuário (exceto senha e roles)
    @Transactional
    public UserResponseDTO updateUser(Long id, UserResponseDTO userUpdateDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));

        // Atualiza apenas os campos permitidos
        existingUser.setEmail(userUpdateDTO.getEmail());
        // Não atualiza username ou senha aqui, pois são tratados por outros fluxos (segurança)

        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserResponseDTO.class);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com ID: " + id));
        userRepository.delete(user);
    }
}
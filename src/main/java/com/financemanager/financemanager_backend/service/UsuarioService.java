package com.financemanager.financemanager_backend.service;

import com.financemanager.financemanager_backend.dto.LoginRequestDTO;
import com.financemanager.financemanager_backend.dto.LoginResponseDTO;
import com.financemanager.financemanager_backend.entity.Usuario;
import com.financemanager.financemanager_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario saveUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("Email já cadastrado: " + usuario.getEmail());
        }
        return usuarioRepository.save(usuario);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        String email = loginRequest.getEmail();
        String senha = loginRequest.getSenha();

        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);

        Usuario usuario = usuarioOptional.get();

        return new LoginResponseDTO(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public Optional<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }
}

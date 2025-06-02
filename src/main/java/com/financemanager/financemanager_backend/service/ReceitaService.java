package com.financemanager.financemanager_backend.service;

import com.financemanager.financemanager_backend.entity.Receita;
import com.financemanager.financemanager_backend.entity.Usuario;
import com.financemanager.financemanager_backend.repository.ReceitaRepository;
import com.financemanager.financemanager_backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Receita saveReceita(Receita receita, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado com ID: " + usuarioId));
        receita.setUsuario(usuario);
        return receitaRepository.save(receita);
    }

    public List<Receita> getReceitasByUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));
        return receitaRepository.findByUsuario(usuario);
    }

    public void deleteReceita(Long receitaId, Long usuarioId) {
        Receita receita = receitaRepository.findById(receitaId)
                .filter(r -> r.getUsuario().getId().equals(usuarioId))
                .orElseThrow(() -> new RuntimeException("Receita não encontrada com ID: " + receitaId));
        receitaRepository.delete(receita);
    }

    public Receita updateReceita(Long receitaId, Receita receitaDetails, Long usuarioId) {
        Receita receita = receitaRepository.findById(receitaId)
                .filter(r -> r.getUsuario().getId().equals(usuarioId))
                .orElseThrow(() -> new RuntimeException("Receita não encontrada com ID: " + receitaId));
        receita.setDescricao(receitaDetails.getDescricao());
        receita.setValor(receitaDetails.getValor());
        receita.setStatus(receitaDetails.getStatus());

        return receitaRepository.save(receita);
    }

    public List<Receita> getReceitasByUsuarioAndMes(Long usuarioId, int mes) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        return receitaRepository.findByUsuarioAndMes(usuario, mes);
    }
}

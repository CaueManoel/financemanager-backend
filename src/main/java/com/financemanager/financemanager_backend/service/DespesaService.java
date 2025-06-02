package com.financemanager.financemanager_backend.service;

import com.financemanager.financemanager_backend.entity.Despesa;
import com.financemanager.financemanager_backend.entity.Usuario;
import com.financemanager.financemanager_backend.repository.DespesaRepository;
import com.financemanager.financemanager_backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Despesa saveDespesa(Despesa despesa, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        despesa.setUsuario(usuario);
        return despesaRepository.save(despesa);
    }

    public List<Despesa> getDespesasByUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return despesaRepository.findByUsuario(usuario);
    }

    public void deleteDespesa(Long despesaId, Long usuarioId) {
        Despesa despesa = despesaRepository.findById(despesaId)
                .filter(d -> d.getUsuario().getId().equals(usuarioId))
                .orElseThrow(() -> new EntityNotFoundException("Despesa não encontrada ou não pertence ao usuário. ID Despesa: " + despesaId));
        despesaRepository.delete(despesa);
    }

    public Despesa updateDespesa(Long despesaId, Despesa updatedDespesa, Long usuarioId) {
        Despesa existingDespesa = despesaRepository.findById(despesaId)
                .filter(r -> r.getUsuario().getId().equals(usuarioId))
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        existingDespesa.setDescricao(updatedDespesa.getDescricao());
        existingDespesa.setValor(updatedDespesa.getValor());
        existingDespesa.setDataVencimento(updatedDespesa.getDataVencimento());
        existingDespesa.setValorPago(updatedDespesa.getValorPago());
        existingDespesa.setStatus(updatedDespesa.getStatus());
        existingDespesa.setParcelas(updatedDespesa.getParcelas());
        return despesaRepository.save(existingDespesa);
    }

    public List<Despesa> getDespesasByUsuarioAndMes(Long usuarioId, int mes) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + usuarioId));

        return despesaRepository.findByUsuarioAndMes(usuario, mes);
    }
}

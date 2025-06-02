package com.financemanager.financemanager_backend.repository;

import com.financemanager.financemanager_backend.entity.Despesa;
import com.financemanager.financemanager_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long> {
    List<Despesa> findByUsuario(Usuario usuario);

    List<Despesa> findByUsuarioAndMes(Usuario usuario, int mes);
}

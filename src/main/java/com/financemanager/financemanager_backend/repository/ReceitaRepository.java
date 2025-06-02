package com.financemanager.financemanager_backend.repository;

import com.financemanager.financemanager_backend.entity.Receita;
import com.financemanager.financemanager_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceitaRepository extends JpaRepository<Receita, Long> {
    List<Receita> findByUsuario(Usuario usuario);

    List<Receita> findByUsuarioAndMes(Usuario usuario, int mes);
}

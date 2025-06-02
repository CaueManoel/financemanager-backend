package com.financemanager.financemanager_backend.controller;

import com.financemanager.financemanager_backend.entity.Receita;
import com.financemanager.financemanager_backend.service.ReceitaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/receitas")
@CrossOrigin(origins = "*")
public class ReceitaController {

    private final ReceitaService receitaService;

    public ReceitaController(ReceitaService receitaService) {
        this.receitaService = receitaService;
    }

    @PostMapping
    public ResponseEntity<?> criarReceita(@PathVariable Long usuarioId, @RequestBody Receita receita) {
        try {
            Receita novaReceita = receitaService.saveReceita(receita, usuarioId);
            return new ResponseEntity<>(novaReceita, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("erro", "Erro ao criar receita: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarReceitasDoUsuario(@PathVariable Long usuarioId, @RequestParam int mes) {
        try {
            if (mes < 1 || mes > 12) {
                return ResponseEntity.badRequest().body(Map.of("erro", "O parâmetro 'mes' deve ser um valor entre 1 e 12."));
            }
            List<Receita> receitas = receitaService.getReceitasByUsuarioAndMes(usuarioId, mes);
            if (receitas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(receitas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro ao buscar receitas."));
        }
    }

    @PutMapping("/{receitaId}")
    public ResponseEntity<?> atualizarReceita(@PathVariable Long usuarioId, @PathVariable Long receitaId, @RequestBody Receita receitaDetalhes) {
        try {
            Receita receitaAtualizada = receitaService.updateReceita(receitaId, receitaDetalhes, usuarioId);
            return ResponseEntity.ok(receitaAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("erro", "Erro ao atualizar receita: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{receitaId}")
    public ResponseEntity<?> deletarReceita(@PathVariable Long usuarioId, @PathVariable Long receitaId) {
        try {
            receitaService.deleteReceita(receitaId, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro ao deletar receita."));
        }
    }
}
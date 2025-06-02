package com.financemanager.financemanager_backend.controller;

import com.financemanager.financemanager_backend.entity.Despesa;
import com.financemanager.financemanager_backend.service.DespesaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/despesas")
@CrossOrigin(origins = "*")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @PostMapping
    public ResponseEntity<?> criarDespesa(@PathVariable Long usuarioId, @Valid @RequestBody Despesa despesa) {
        try {
            Despesa novaDespesa = despesaService.saveDespesa(despesa, usuarioId);
            return new ResponseEntity<>(novaDespesa, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> listarDespesasDoUsuario(@PathVariable Long usuarioId, @RequestParam int mes) {
        try {
            if (mes < 1 || mes > 12) {
                return ResponseEntity.badRequest().body(Map.of("erro", "O parâmetro 'mes' deve ser um valor entre 1 e 12."));
            }
            List<Despesa> despesas = despesaService.getDespesasByUsuarioAndMes(usuarioId, mes);
            if (despesas.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(despesas);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("erro", "Erro ao buscar despesas."));
        }
    }

    @PutMapping("/{despesaId}")
    public ResponseEntity<?> atualizarDespesa(
            @PathVariable Long usuarioId,
            @PathVariable Long despesaId,
            @Valid @RequestBody Despesa despesaDetalhes) {
        try {
            Despesa despesaAtualizada = despesaService.updateDespesa(despesaId, despesaDetalhes, usuarioId);
            return ResponseEntity.ok(despesaAtualizada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @DeleteMapping("/{despesaId}")
    public ResponseEntity<Void> deletarDespesa(
            @PathVariable Long usuarioId,
            @PathVariable Long despesaId) {
        try {
            despesaService.deleteDespesa(despesaId, usuarioId);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
package br.com.escola.armarios.controller;

import br.com.escola.armarios.model.ReservaArmario;
import br.com.escola.armarios.service.ReservaArmarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API REST de reservas de armário.
 * O @CrossOrigin libera o acesso do front-end Angular (http://localhost:4200).
 */
@RestController
@RequestMapping("/api/armarios")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ReservaArmarioController {

    private final ReservaArmarioService service;

    /** GET /api/armarios — lista todas as reservas */
    @GetMapping
    public List<ReservaArmario> listar() {
        return service.listar();
    }

    /** GET /api/armarios/{id} — detalhes de uma reserva */
    @GetMapping("/{id}")
    public ReservaArmario buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    /** POST /api/armarios — cria a reserva validando se o armário está livre */
    @PostMapping
    public ResponseEntity<ReservaArmario> criar(@RequestBody ReservaArmario reserva) {
        ReservaArmario criada = service.criarReserva(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    /** PUT /api/armarios/{id} — edita dados ou datas da reserva */
    @PutMapping("/{id}")
    public ReservaArmario atualizar(@PathVariable Long id, @RequestBody ReservaArmario reserva) {
        return service.atualizar(id, reserva);
    }

    /** DELETE /api/armarios/{id} — exclui/cancela a reserva */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    /** PATCH /api/armarios/{id}/liberar — marca a reserva como encerrada/inativa */
    @PatchMapping("/{id}/liberar")
    public ReservaArmario liberar(@PathVariable Long id) {
        return service.liberar(id);
    }
}

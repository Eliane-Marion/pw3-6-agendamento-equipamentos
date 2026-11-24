package br.com.etec.rafaelmn_eduardo.equipamento.controller;

import br.com.etec.rafaelmn_eduardo.equipamento.dto.ReservaArmarioResponseDTO;
import br.com.etec.rafaelmn_eduardo.equipamento.entity.ReservaArmario;
import br.com.etec.rafaelmn_eduardo.equipamento.mapper.ReservaArmarioMapper;
import br.com.etec.rafaelmn_eduardo.equipamento.service.ReservaArmarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/armarios")
public class ReservaArmarioController {
    private final ReservaArmarioService service;
    private final ReservaArmarioMapper mapper;

    public ReservaArmarioController(ReservaArmarioService service, ReservaArmarioMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    //Get -> Listas todas reservas
    public List<ReservaArmarioResponseDTO> listarTodos() {
        return service.listarTodos().stream().map(mapper::toDTO).toList();
    }

    //Get -> Buscar por Id
    @GetMapping("/{id}")
    public ReservaArmarioResponseDTO buscarPorId(@PathVariable Long id) {
        return mapper.toDTO(service.buscarPorId(id));
    }

    //Post -> Cadastrar reserva
    @PostMapping
    public ResponseEntity<ReservaArmarioResponseDTO> cadastrar(@RequestBody ReservaArmario reserva) {
        ReservaArmario salva = service.cadastrar(reserva);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDTO(salva));
    }

    //Put -> Atualizar reserva
    @PutMapping("/{id}")
    public ReservaArmarioResponseDTO atualizar(@PathVariable Long id, @RequestBody ReservaArmario reserva) {
        return mapper.toDTO(service.atualizar(id, reserva));
    }

    //Delete -> Excluir reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);

        return ResponseEntity.noContent().build();
    }

    //Patch -> Liberar armário
    @PatchMapping("/{id}/liberar")
    public ReservaArmarioResponseDTO liberar(@PathVariable Long id) {
        return mapper.toDTO(service.liberar(id));
    }
}

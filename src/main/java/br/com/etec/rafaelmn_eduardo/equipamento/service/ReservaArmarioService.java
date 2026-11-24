package br.com.etec.rafaelmn_eduardo.equipamento.service;

import br.com.etec.rafaelmn_eduardo.equipamento.entity.ReservaArmario;
import br.com.etec.rafaelmn_eduardo.equipamento.repository.ReservaArmarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaArmarioService {
    private final ReservaArmarioRepository repository;

    public ReservaArmarioService(ReservaArmarioRepository repository) {
        this.repository = repository;
    }

    //Validar datas
    private void validarData(ReservaArmario reserva) {
        if (reserva.getNmrArmario() == null
            || reserva.getNmAluno() == null
            || reserva.getDtInicio() == null
            || reserva.getDtFim() == null
            || reserva.getNmAluno().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Preencha todos os campos obrigatórios");
        }

        if(!reserva.getDtInicio().isAfter(reserva.getDtFim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A data inicial deve ser anterior à data final");
        }
    }

    //Getter -> listar todas reservas
    public List<ReservaArmario> listarTodos(){
        return repository.findAll();
    }

    //Getter -> buscar por Id
    public ReservaArmario buscarPorId(Long id){
        return repository.findById(id).orElse(null);
    }

    //Post -> Criar reserva
    public ReservaArmario cadastrar(ReservaArmario reserva){

        validarData(reserva);

        boolean conflito = repository.existsByNmrArmarioAndAtvTrueAndDtInicioLessThanAndDtFimGreaterThan(
                reserva.getNmrArmario(),
                reserva.getDtInicio(),
                reserva.getDtFim()
        );

        if(conflito){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "O armário já possui uma reserva nesse período");
        }

        reserva.setAtv(true);

        return repository.save(reserva);
    }

    //Put -> Atualizar reserva
    public ReservaArmario atualizar(Long id, ReservaArmario dados){
        ReservaArmario reserva = buscarPorId(id);

        validarData(dados);

        boolean conflito = repository.existsByNmrArmarioAndAtvTrueAndDtInicioLessThanAndDtFimGreaterThanAndIdNot(
                dados.getNmrArmario(),
                dados.getDtInicio(),
                dados.getDtFim(),
                id
        );

        if(conflito){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outra reserva nesse período");
        }

        reserva.setNmrArmario(dados.getNmrArmario());
        reserva.setNmAluno(dados.getNmAluno());
        reserva.setDtInicio(dados.getDtInicio());
        reserva.setDtFim(dados.getDtFim());

        return repository.save(reserva);
    }

    //Delete -> Excluir reserva
    public void excluir(Long id){
        ReservaArmario reserva = buscarPorId(id);
        repository.delete(reserva);
    }

    //Patch -> Liberar armário
    public ReservaArmario liberar(Long id){
        ReservaArmario reserva = buscarPorId(id);

        reserva.setAtv(false);

        return repository.save(reserva);
    }
}

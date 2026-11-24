package br.com.escola.armarios.service;

import br.com.escola.armarios.exception.DadosInvalidosException;
import br.com.escola.armarios.exception.RecursoNaoEncontradoException;
import br.com.escola.armarios.exception.RegraNegocioException;
import br.com.escola.armarios.model.ReservaArmario;
import br.com.escola.armarios.repository.ReservaArmarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Camada de regra de negócio das reservas de armário.
 */
@Service
@RequiredArgsConstructor
public class ReservaArmarioService {

    static final String MSG_CONFLITO = "O armário já possui uma reserva ativa para o período informado.";

    private final ReservaArmarioRepository repository;

    public List<ReservaArmario> listar() {
        return repository.findAll();
    }

    public ReservaArmario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva " + id + " não encontrada."));
    }

    /**
     * Regra de negócio central (pseudocódigo criarReserva):
     * só cria a reserva se o armário estiver livre no período informado.
     */
    @Transactional
    public ReservaArmario criarReserva(ReservaArmario novaReserva) {
        validarCampos(novaReserva);
        verificarConflito(novaReserva, null);

        novaReserva.setId(null);      // o id é sempre gerado pelo banco
        novaReserva.setAtivo(true);   // novaReserva.ativo = Verdadeiro
        return repository.save(novaReserva);
    }

    /**
     * Edita os dados ou as datas da reserva. Se a reserva continuar ativa,
     * o novo período também precisa estar livre (ignorando a própria reserva).
     */
    @Transactional
    public ReservaArmario atualizar(Long id, ReservaArmario dados) {
        ReservaArmario existente = buscarPorId(id);
        validarCampos(dados);

        existente.setNumeroArmario(dados.getNumeroArmario());
        existente.setNomeAluno(dados.getNomeAluno().trim());
        existente.setDataInicio(dados.getDataInicio());
        existente.setDataFim(dados.getDataFim());

        if (Boolean.TRUE.equals(existente.getAtivo())) {
            verificarConflito(existente, existente.getId());
        }
        return repository.save(existente);
    }

    @Transactional
    public void excluir(Long id) {
        ReservaArmario existente = buscarPorId(id);
        repository.delete(existente);
    }

    /**
     * PATCH /liberar: encerra a reserva (ativo = false), liberando o armário.
     */
    @Transactional
    public ReservaArmario liberar(Long id) {
        ReservaArmario existente = buscarPorId(id);
        if (!Boolean.TRUE.equals(existente.getAtivo())) {
            throw new RegraNegocioException("Esta reserva já está encerrada.");
        }
        existente.setAtivo(false);
        return repository.save(existente);
    }

    // ------------------------------------------------------------------ regras

    /**
     * Para cada reserva ativa do mesmo armário: se
     * (nova.dataInicio < reserva.dataFim) E (nova.dataFim > reserva.dataInicio), há conflito.
     */
    private void verificarConflito(ReservaArmario novaReserva, Long idIgnorado) {
        List<ReservaArmario> reservasExistentes =
                repository.findByNumeroArmarioAndAtivoTrue(novaReserva.getNumeroArmario());

        for (ReservaArmario reserva : reservasExistentes) {
            if (idIgnorado != null && Objects.equals(reserva.getId(), idIgnorado)) {
                continue; // na edição, a reserva não conflita com ela mesma
            }
            if (periodosSeSobrepoem(novaReserva, reserva)) {
                throw new RegraNegocioException(MSG_CONFLITO);
            }
        }
    }

    static boolean periodosSeSobrepoem(ReservaArmario nova, ReservaArmario existente) {
        return nova.getDataInicio().isBefore(existente.getDataFim())
                && nova.getDataFim().isAfter(existente.getDataInicio());
    }

    private void validarCampos(ReservaArmario r) {
        if (r == null) {
            throw new DadosInvalidosException("Os dados da reserva são obrigatórios.");
        }
        if (r.getNumeroArmario() == null || r.getNumeroArmario() < 1) {
            throw new DadosInvalidosException("O número do armário deve ser maior ou igual a 1.");
        }
        if (r.getNomeAluno() == null || r.getNomeAluno().trim().length() < 3) {
            throw new DadosInvalidosException("O nome do aluno deve ter pelo menos 3 caracteres.");
        }
        if (r.getDataInicio() == null || r.getDataFim() == null) {
            throw new DadosInvalidosException("As datas de início e de fim são obrigatórias.");
        }
        if (!r.getDataFim().isAfter(r.getDataInicio())) {
            throw new DadosInvalidosException("A data de fim deve ser posterior à data de início.");
        }
        r.setNomeAluno(r.getNomeAluno().trim());
    }
}

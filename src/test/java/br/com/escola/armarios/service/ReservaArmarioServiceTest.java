package br.com.escola.armarios.service;

import br.com.escola.armarios.exception.DadosInvalidosException;
import br.com.escola.armarios.exception.RegraNegocioException;
import br.com.escola.armarios.model.ReservaArmario;
import br.com.escola.armarios.repository.ReservaArmarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testes unitários da regra de conflito de reservas (sem subir o Spring).
 */
class ReservaArmarioServiceTest {

    private ReservaArmarioRepository repository;
    private ReservaArmarioService service;

    private final ReservaArmario existente = new ReservaArmario(
            1L, 10, "Ana", LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 20), true);

    @BeforeEach
    void setUp() {
        repository = mock(ReservaArmarioRepository.class);
        service = new ReservaArmarioService(repository);
        when(repository.findByNumeroArmarioAndAtivoTrue(10)).thenReturn(List.of(existente));
        when(repository.save(any(ReservaArmario.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    private ReservaArmario nova(int armario, LocalDate inicio, LocalDate fim) {
        return new ReservaArmario(null, armario, "Bruno", inicio, fim, null);
    }

    @Test
    void deveBloquearReservaComPeriodoSobreposto() {
        ReservaArmario r = nova(10, LocalDate.of(2026, 11, 15), LocalDate.of(2026, 11, 25));
        RegraNegocioException ex = assertThrows(RegraNegocioException.class, () -> service.criarReserva(r));
        assertEquals(ReservaArmarioService.MSG_CONFLITO, ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void devePermitirReservaQueComecaNoDiaEmQueAOutraTermina() {
        ReservaArmario r = nova(10, LocalDate.of(2026, 11, 20), LocalDate.of(2026, 11, 30));
        ReservaArmario salva = service.criarReserva(r);
        assertTrue(salva.getAtivo());
    }

    @Test
    void devePermitirOutroArmarioNoMesmoPeriodo() {
        ReservaArmario r = nova(11, LocalDate.of(2026, 11, 12), LocalDate.of(2026, 11, 18));
        assertTrue(service.criarReserva(r).getAtivo());
    }

    @Test
    void deveRecusarDataFimAntesDoInicio() {
        ReservaArmario r = nova(10, LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 1));
        assertThrows(DadosInvalidosException.class, () -> service.criarReserva(r));
    }

    @Test
    void edicaoNaoConflitaComAPropriaReserva() {
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        ReservaArmario dados = nova(10, LocalDate.of(2026, 11, 12), LocalDate.of(2026, 11, 22));
        ReservaArmario atualizada = service.atualizar(1L, dados);
        assertEquals(LocalDate.of(2026, 11, 22), atualizada.getDataFim());
    }

    @Test
    void liberarMarcaComoInativa() {
        ReservaArmario ativa = new ReservaArmario(2L, 5, "Carla",
                LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 5), true);
        when(repository.findById(2L)).thenReturn(Optional.of(ativa));
        assertFalse(service.liberar(2L).getAtivo());
        assertThrows(RegraNegocioException.class, () -> service.liberar(2L));
    }
}

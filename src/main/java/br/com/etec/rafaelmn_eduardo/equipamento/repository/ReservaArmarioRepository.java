package br.com.etec.rafaelmn_eduardo.equipamento.repository;

import br.com.etec.rafaelmn_eduardo.equipamento.entity.ReservaArmario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaArmarioRepository extends JpaRepository<ReservaArmario, Long> {

    //Verificar se existe reserva conflitando
    boolean existsByNmrArmarioAndAtvTrueAndDtInicioLessThanAndDtFimGreaterThan(
            Integer nmrArmario,
            java.time.LocalDate dtInicio,
            java.time.LocalDate dtFim
    );

    //Verificar se existe reserva conflitando na atualização
    boolean existsByNmrArmarioAndAtvTrueAndDtInicioLessThanAndDtFimGreaterThanAndIdNot(
            Integer nmrArmario,
            java.time.LocalDate dtInicio,
            java.time.LocalDate dtFim,
            Long id
    );
}

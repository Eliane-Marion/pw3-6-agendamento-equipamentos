package br.com.etec.rafaelmn_eduardo.equipamento.mapper;

import br.com.etec.rafaelmn_eduardo.equipamento.dto.ReservaArmarioResponseDTO;
import br.com.etec.rafaelmn_eduardo.equipamento.entity.ReservaArmario;
import org.springframework.stereotype.Component;

@Component
public class ReservaArmarioMapper {
    public ReservaArmarioResponseDTO toDTO(ReservaArmario reserva) {
        return new ReservaArmarioResponseDTO(
                reserva.getId(),
                reserva.getNmrArmario(),
                reserva.getNmAluno(),
                reserva.getDtInicio(),
                reserva.getDtFim(),
                reserva.getAtv()
        );
    }
}

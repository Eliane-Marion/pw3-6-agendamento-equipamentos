package br.com.etec.rafaelmn_eduardo.equipamento.dto;

import java.time.LocalDate;

public record ReservaArmarioResponseDTO(
        Long id,
        Integer nmrArmario,
        String nmAluno,
        LocalDate dtInicio,
        LocalDate dtFim,
        Boolean atv
) {
}

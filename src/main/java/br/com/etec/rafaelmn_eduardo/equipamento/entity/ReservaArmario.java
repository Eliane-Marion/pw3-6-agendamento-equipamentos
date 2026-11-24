package br.com.etec.rafaelmn_eduardo.equipamento.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "reserva_armario")
@Getter
@Setter
public class ReservaArmario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numeroArmario")
    private Integer nmrArmario;

    @Column(name = "nomeAluno")
    private String nmAluno;

    @Column(name = "dataInicio")
    private LocalDate dtInicio;

    @Column(name = "dataFim")
    private LocalDate dtFim;

    @Column(name = "ativo")
    private Boolean atv = true;
}

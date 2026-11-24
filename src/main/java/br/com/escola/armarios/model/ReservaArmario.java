package br.com.escola.armarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entidade principal: reserva de um armário por um aluno em um período.
 */
@Entity
@Table(name = "reserva_armario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaArmario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numeroArmario;

    @Column(nullable = false, length = 100)
    private String nomeAluno;

    @Column(nullable = false)
    private LocalDate dataInicio;

    @Column(nullable = false)
    private LocalDate dataFim;

    @Column(nullable = false)
    private Boolean ativo;
}

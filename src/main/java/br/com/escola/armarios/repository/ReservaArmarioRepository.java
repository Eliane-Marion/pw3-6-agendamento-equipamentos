package br.com.escola.armarios.repository;

import br.com.escola.armarios.model.ReservaArmario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaArmarioRepository extends JpaRepository<ReservaArmario, Long> {

    /**
     * buscarAtivasPorArmario: todas as reservas ativas de um armário.
     * O Spring Data gera a consulta automaticamente a partir do nome do método:
     * SELECT * FROM reserva_armario WHERE numero_armario = ? AND ativo = true
     */
    List<ReservaArmario> findByNumeroArmarioAndAtivoTrue(Integer numeroArmario);
}

package usco.edu.co.parcial2.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import usco.edu.co.parcial2.model.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByMedicoUsuarioUsernameOrderByFechaAscHoraInicioAsc(String username);

    List<Consulta> findByPacienteUsuarioUsernameOrderByFechaAscHoraInicioAsc(String username);

    @Query("""
            select count(c) > 0
            from Consulta c
            where (:ignorarId is null or c.id <> :ignorarId)
              and c.fecha = :fecha
              and (c.consultorio.id = :consultorioId or c.medico.id = :medicoId)
              and c.horaInicio < :horaFin
              and :horaInicio < c.horaFin
            """)
    boolean existeCruce(@Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("consultorioId") Long consultorioId,
            @Param("medicoId") Long medicoId,
            @Param("ignorarId") Long ignorarId);
}


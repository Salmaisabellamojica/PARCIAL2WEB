package usco.edu.co.parcial2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import usco.edu.co.parcial2.model.Consultorio;

public interface ConsultorioRepository extends JpaRepository<Consultorio, Long> {
    Optional<Consultorio> findByNumero(Integer numero);
}


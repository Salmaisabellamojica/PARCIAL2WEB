package usco.edu.co.parcial2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import usco.edu.co.parcial2.model.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByUsuarioUsername(String username);

    Optional<Paciente> findByNombreIgnoreCase(String nombre);
}


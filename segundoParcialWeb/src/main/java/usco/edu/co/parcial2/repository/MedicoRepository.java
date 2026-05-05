package usco.edu.co.parcial2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import usco.edu.co.parcial2.model.Medico;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
    Optional<Medico> findByUsuarioUsername(String username);
}


package usco.edu.co.parcial2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import usco.edu.co.parcial2.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}


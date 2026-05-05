package usco.edu.co.parcial2.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import usco.edu.co.parcial2.dto.MedicoRequest;
import usco.edu.co.parcial2.model.Medico;
import usco.edu.co.parcial2.service.MedicoService;

@RestController
@RequestMapping("/api/medicos")
@Tag(name = "Medicos", description = "Servicios web para crear, editar, eliminar y listar medicos")
public class MedicoApiController {

    private final MedicoService medicoService;

    public MedicoApiController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Listar medicos", description = "Retorna todos los medicos registrados.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public List<Medico> listar() {
        return medicoService.listar();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Crear medico", description = "Crea un medico con usuario de acceso y validaciones regex en el JSON.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Medico crear(@Valid @RequestBody MedicoRequest request) {
        return medicoService.crear(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Editar medico", description = "Actualiza los datos del medico y su usuario.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Medico actualizar(@PathVariable Long id, @Valid @RequestBody MedicoRequest request) {
        return medicoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Eliminar medico", description = "Elimina un medico existente.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public void eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
    }
}


package usco.edu.co.parcial2.controller;

import java.security.Principal;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import usco.edu.co.parcial2.dto.ConsultaRequest;
import usco.edu.co.parcial2.dto.HorarioRequest;
import usco.edu.co.parcial2.model.Consulta;
import usco.edu.co.parcial2.service.ConsultaService;

@RestController
@RequestMapping("/api/consultas")
@Tag(name = "Consultas", description = "Servicios web para gestionar consultas medicas")
public class ConsultaApiController {

    private final ConsultaService consultaService;

    public ConsultaApiController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    @Operation(summary = "Listar consultas", description = "Administrador ve todas, medico ve solo sus consultas, paciente ve solo sus citas.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public List<Consulta> listar(Authentication authentication) {
        if (tieneRol(authentication, "MEDICO")) {
            return consultaService.listarPorMedico(authentication.getName());
        }
        if (tieneRol(authentication, "PACIENTE")) {
            return consultaService.listarPorPaciente(authentication.getName());
        }
        return consultaService.listarTodas();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Crear consulta", description = "Registra una consulta y valida que no exista cruce en el mismo dia, hora, consultorio o medico.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Consulta crear(@Valid @RequestBody ConsultaRequest request) {
        return consultaService.crear(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Editar consulta", description = "Actualiza todos los campos de una consulta.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Consulta actualizar(@PathVariable Long id, @Valid @RequestBody ConsultaRequest request) {
        return consultaService.actualizar(id, request);
    }

    @PatchMapping("/{id}/horario")
    @PreAuthorize("hasAuthority('PACIENTE')")
    @Operation(summary = "Actualizar horario de cita", description = "El paciente solo puede actualizar fecha y horario de las citas asignadas a el.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public Consulta actualizarHorario(@PathVariable Long id, @Valid @RequestBody HorarioRequest request, Principal principal) {
        return consultaService.actualizarHorarioPaciente(id, request, principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ADMINISTRADOR')")
    @Operation(summary = "Eliminar consulta", description = "Elimina una consulta existente.")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    public void eliminar(@PathVariable Long id) {
        consultaService.eliminar(id);
    }

    private boolean tieneRol(Authentication authentication, String rol) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(rol));
    }
}


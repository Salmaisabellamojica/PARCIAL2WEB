package usco.edu.co.parcial2.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ConsultaRequest(
        @NotBlank
        @Size(max = 40)
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]{1,40}$", message = "El nombre del paciente debe ser alfanumerico y no superar 40 caracteres")
        String nombrePaciente,

        @NotBlank
        @Size(max = 100)
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ,.:-]{1,100}$", message = "El motivo solo permite texto alfanumerico basico y maximo 100 caracteres")
        String motivo,

        @NotBlank
        @Pattern(regexp = "^[0-9]{1,5}$", message = "El numero de consultorio debe ser numerico")
        String numeroConsultorio,

        @NotNull
        @FutureOrPresent
        LocalDate fecha,

        @NotNull
        LocalTime horaInicio,

        @NotNull
        LocalTime horaFin,

        @NotNull
        Long medicoId) {
}

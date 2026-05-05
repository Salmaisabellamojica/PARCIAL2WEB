package usco.edu.co.parcial2.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record HorarioRequest(
        @NotNull
        @FutureOrPresent
        LocalDate fecha,

        @NotNull
        LocalTime horaInicio,

        @NotNull
        LocalTime horaFin) {
}


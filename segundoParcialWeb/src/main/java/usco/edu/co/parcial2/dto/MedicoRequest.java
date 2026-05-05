package usco.edu.co.parcial2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MedicoRequest(
        @NotBlank
        @Size(max = 40)
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]{1,40}$", message = "El nombre debe ser alfanumerico y maximo de 40 caracteres")
        String nombre,

        @NotBlank
        @Size(max = 60)
        @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]{1,60}$", message = "La especialidad debe ser alfanumerica")
        String especialidad,

        @NotBlank
        @Pattern(regexp = "^[0-9]{5,20}$", message = "El documento debe contener entre 5 y 20 numeros")
        String documento,

        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9._-]{4,40}$", message = "El usuario solo permite letras, numeros, punto, guion y guion bajo")
        String username,

        @Size(max = 60)
        String password) {
}

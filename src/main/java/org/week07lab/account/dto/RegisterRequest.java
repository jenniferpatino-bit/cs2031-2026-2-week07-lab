package org.week07lab.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotBlank(message = "El email es obligatorio")
        @Email(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "El email no es valido")
        String email,

        @NotBlank(message = "El nombre es obligatorio")
        @Pattern(regexp = ".*[A-Z].*", message = "El nombre debe contener al menos una letra mayuscula")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        @Pattern(regexp = ".*[A-Z].*", message = "El apellido debe contener al menos una letra mayuscula")
        String lastName,

        @NotBlank(message = "La contrasena es obligatoria")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$",
                message = "La contrasena debe tener minimo 8 caracteres, al menos 1 letra y 1 numero")
        String password
) {
}

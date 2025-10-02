package com.unicartagena.SyncNotes.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    @NotBlank(message = "Nombre y apellido son obligatorios")
    private String name;

    @NotBlank(message = "Nombre de usuario es obligatorio")
    @Size(min = 6, max = 15, message = "El nombre de usuario debe tener de 6 a 15 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.]+$", message = "Nombre de usuario solo puede contener letras, números, guiones bajos y puntos")
    private String username;
}

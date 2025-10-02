package com.unicartagena.SyncNotes.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String id;

    @NotBlank(message = "Nombre y apellido son obligatorios")
    private String name;

    @NotBlank(message = "Nombre de usuario es obligatorio")
    @Size(min = 6, max = 15, message = "El nombre de usuario debe tener de 6 a 15 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9_.]+$", message = "Nombre de usuario solo puede contener letras, números, guiones bajos y puntos")
    private String username;

    @NotBlank(message = "Contraseña es obligatoria")
    @Size(min = 8, max = 20, message = "Contraseña debe tener entre 8 y 20 caracteres")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,20}$",
            message = "La contraseña debe tener al menos una letra mayúscula, una minúscula, un número y un carácter especial"
    )
    private String password;


}

package ru.adel.deliveryapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationDTO {
    @NotBlank
    @Email
    private String username; //формат email

    private String password;


}

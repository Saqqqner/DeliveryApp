package ru.adel.deliveryapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerDTO {
    private Long id;
    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String email;

    @NotNull(message = "Адрес необходим для заполнения")
    @Pattern(regexp="^[\\p{L}\\d\\s-,]+?,\\s*[\\p{L}\\d\\s-,]+?,\\s*[\\p{L}\\d\\s-,]+?,\\s*[\\p{L}\\d]+(?:\\s*[\\p{L}\\d]+)?$",
            message="Address must be in the format: Country, City, Street, House number, Apartment number")
    private String address;

    @NotBlank
    @Size(min = 6, max = 255)
    private String password;


}


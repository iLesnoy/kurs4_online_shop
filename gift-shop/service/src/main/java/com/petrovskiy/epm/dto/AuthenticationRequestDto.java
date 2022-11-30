package com.petrovskiy.epm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequestDto {

    @NotEmpty
    @NotBlank(message = "name is mandatory")
    @Pattern(regexp = "^[\\p{Alpha}А-Яа-я]{2,65}$")
    private String name;

    @NotBlank(message = "password is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z]|[A-Z]).{8,20}$",message = "password example hhelloPAPA12345")
    private String password;
}

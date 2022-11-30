package com.petrovskiy.epm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.petrovskiy.epm.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonPropertyOrder({"id", "firstName", "lastName","email","password"})
public class UserDto extends RepresentationModel<UserDto> {

    @JsonProperty("id")
    private Long id;

    @NotEmpty
    @NotBlank(message = "firstName is mandatory")
    @Pattern(regexp = "^[\\p{Alpha}А-Яа-я]{2,65}$")
    @JsonProperty("firstName")
    private String firstName;

    @NotBlank(message = "lastName is mandatory")
    @Pattern(regexp = "^[\\p{Alpha}А-Яа-я]{2,65}$")
    @JsonProperty("lastName")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "password is mandatory")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z]|[A-Z]).{8,20}$",message = "password example hhelloPAPA12345")
    @JsonProperty(access = WRITE_ONLY)
    private String password;

    @JsonProperty("role")
    private Role role;
}

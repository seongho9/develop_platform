package me.seongho9.dev.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupDTO {

    @NotBlank
    private String userId;
    @NotBlank
    private String passwd;
    @NotBlank
    private String name;
    @NotBlank
    @Email
    private String mail;
}

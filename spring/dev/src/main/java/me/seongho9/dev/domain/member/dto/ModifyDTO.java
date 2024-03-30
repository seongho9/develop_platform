package me.seongho9.dev.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ModifyDTO {

    @NotNull
    private String userId;
    @NotBlank
    private String passwdBefore;
    @NotBlank
    private String passwdAfter;
    @Email
    private String mail;
}

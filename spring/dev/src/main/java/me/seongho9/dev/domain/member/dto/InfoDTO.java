package me.seongho9.dev.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InfoDTO {

    @NotBlank
    private String userId;
}

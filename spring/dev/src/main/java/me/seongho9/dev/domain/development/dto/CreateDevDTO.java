package me.seongho9.dev.domain.development.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDevDTO {

    private String userId;
    @NotBlank
    private String imageName;

}

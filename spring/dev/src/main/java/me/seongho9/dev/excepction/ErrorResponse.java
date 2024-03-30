package me.seongho9.dev.excepction;

import lombok.Data;

@Data
public class ErrorResponse {

    private final Integer status;
    private final String message;
}

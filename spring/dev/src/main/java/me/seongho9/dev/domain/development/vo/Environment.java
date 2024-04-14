package me.seongho9.dev.domain.development.vo;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Environment {
    private final String id;
    private final String userId;
    private final String name;
    private final boolean isRunning;
}

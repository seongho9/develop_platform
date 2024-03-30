package me.seongho9.dev.domain.member.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InfoVO {

    private String id;
    private String userName;
    private String mail;
}

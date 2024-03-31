package me.seongho9.dev.service.server;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServerServiceJSchTest {

    @Autowired
    ServerService serverService;

    @Test
    void rm() {
        //given
        String path = "/home/seongho/test_spring";
        serverService.mkdir(path);
        //when
        serverService.rm(path);
        //then
        Assertions.assertThat(serverService.ls("/home/seongho")).isNotIn("test_spring");
    }
    @Test
    void mkdir() {
        //given
        String path = "/home/seongho/test_spring";
        //when
        serverService.mkdir(path);
        //then
        Assertions.assertThat(serverService.ls("/home/seongho")).contains("test_spring");
        //after
        serverService.rm("/home/seongho/test_spring");
    }
    @Test
    void ls() {
        //given
        String path = "/home/seongho";
        //when
        List<String> ls = serverService.ls(path);
        //then
        Assertions.assertThat(ls).contains("dev_pub.pem");
    }
}
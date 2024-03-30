package me.seongho9.dev.service.server;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ServerPowerServiceImplTest {

    @Autowired
    ServerPowerService powerService;

    @Test
    void powerOn() {

        //given
        String ip = "192.168.0.5";
        String mac = "00:16:96:ec:6b:36";
        //when
        powerService.powerOn(ip, mac);
        //then
    }

    @Test
    void pingIfPowerOn(){
        //given
        String ip = "192.168.0.5";
        //when
        boolean ping = powerService.ping(ip);
        //then
        Assertions.assertThat(ping).isEqualTo(true);
    }
    @Test
    void pingIfPowerOff() {
        //given
        String ip = "192.168.0.5";
        //when
        boolean ping = powerService.ping(ip);
        //then
        Assertions.assertThat(ping).isEqualTo(false);
    }
}
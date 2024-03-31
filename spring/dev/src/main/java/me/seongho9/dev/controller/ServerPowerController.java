package me.seongho9.dev.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.service.server.ServerPowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerPowerController {

    @Autowired
    private final ServerPowerService powerService;
    @Value("${host.ip}")
    String ip;
    @Value("${host.mac}")
    String mac;

    @PostMapping("/on")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String powerOn() {
        log.info("ip:{}", ip);
        log.info("mac:{}", mac);
        powerService.powerOn(ip, mac);

        return "power on";
    }

    @GetMapping("/ping")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public String ping(){
        if(powerService.ping(ip)){
            return "server is on";
        }
        else{
            return "server is off";
        }
    }
}

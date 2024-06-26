package me.seongho9.dev.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.domain.container.Container;
import me.seongho9.dev.domain.development.dto.CreateDevDTO;
import me.seongho9.dev.domain.development.dto.DeleteEnvDTO;
import me.seongho9.dev.domain.development.vo.Environment;
import me.seongho9.dev.service.development.DevelopmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/dev")
@RequiredArgsConstructor
public class DevelopmentController {

    @Autowired
    private final DevelopmentService developmentService;

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public String createContainer(@RequestBody CreateDevDTO createDevDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        createDevDTO.setUserId(name);
        developmentService.createEnvironment(createDevDTO);

        return "success to create container " + createDevDTO.getImageName();
    }

    @GetMapping("/list")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<Environment> getDevList(@RequestHeader("Authorization") String token) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        List<Environment> environmentList = developmentService.getEnvironmentList(name);

        return environmentList;
    }

    @GetMapping("/images")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<String> getImageList() {
        List<String> imageList = developmentService.getImageList();

        return imageList;
    }

    @PostMapping("/start")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public String startDev(@RequestParam("id") String id) {
        developmentService.startEnvironment(id);

        return "success start " + id;
    }

    @PostMapping("/stop")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public String stopDev(@RequestParam("id") String id) {
        developmentService.stopEnvironment(id);

        return "success stop " + id;
    }

    @DeleteMapping("/delete")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public String deleteDev(@RequestBody DeleteEnvDTO deleteEnvDTO) {
        developmentService.deleteEnvironment(deleteEnvDTO.getId(), deleteEnvDTO.getImageName());

        return "success delete " + deleteEnvDTO.getId();
    }
}

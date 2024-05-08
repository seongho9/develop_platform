package me.seongho9.dev.service.container.task;

import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.domain.ExposePorts;
import me.seongho9.dev.domain.member.Member;
import me.seongho9.dev.domain.member.dto.SignupDTO;
import me.seongho9.dev.repository.MemberRepository;
import me.seongho9.dev.repository.PortRepository;
import me.seongho9.dev.service.development.task.TaskManager;
import me.seongho9.dev.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@SpringBootTest
class TaskManagerTest {

    @Autowired
    TaskManager taskManager;
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PortRepository portRepository;

    @BeforeEach
    void before(){
        memberRepository.deleteAll();
        portRepository.deleteAll();
        ExposePorts ports = new ExposePorts();
        ports.setStart(0); ports.setCurrent(0); ports.setEnd(0);
        portRepository.save(ports);
    }
    @Test
    void logPrint(){
    }

    @Test
    void rollbackSignup(){
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserId("seongho9");
        signupDTO.setPasswd("1234");
        signupDTO.setName("seonghoJang");
        signupDTO.setMail("seongho9276@gmail.com");

        memberService.signup(signupDTO);

        Queue<Function<String,String>> q= new LinkedList<>();
        Function<String,String> undoSignup = (String param) -> {
            memberRepository.deleteById("seongho9");
            log.info("on function");
            return null;
        };
        q.add(undoSignup);
        Assertions.assertThat(memberRepository.findById("seongho9").get().getMail())
                .isEqualTo(signupDTO.getMail());
        taskManager.undo(q);
        Optional<Member> byId = memberRepository.findById("seongho9");

    }
}
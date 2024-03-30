package me.seongho9.dev.service.container.task;

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
        Queue<Function<String, String>> q = new LinkedList<>();
        Function<String, Integer> undoTask = (string) ->{
            System.out.println(string);
            return 1;
        };
        taskManager.registerTask(q, undoTask);

        Queue<String> strings = new LinkedList<>();
        strings.add("1234");
        taskManager.undo(q, strings);
    }

    @Test
    void rollbackSignup(){
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setUserId("seongho9");
        signupDTO.setPasswd("1234");
        signupDTO.setName("seonghoJang");
        signupDTO.setMail("seongho9276@gmail.com");
        memberService.signup(signupDTO);
        Queue<Function<String, String>> q = new LinkedList<>();
        Function<String, String> undoSignup = (id) -> {
            Member member = new Member();
            member.setId(id);
            memberRepository.delete(member);
            return null;
        };
        Queue<String> paramList = new LinkedList<>();
        paramList.add("seongho9");
        taskManager.registerTask(q, undoSignup);

        Assertions.assertThat(memberRepository.findById("seongho9").get().getMail())
                .isEqualTo(signupDTO.getMail());
        taskManager.undo(q, paramList);

        Assertions.assertThat(memberRepository.findById("seongho9").isEmpty()).isEqualTo(true);

    }
}
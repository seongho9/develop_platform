package me.seongho9.dev.service.member;

import me.seongho9.dev.domain.ExposePorts;
import me.seongho9.dev.domain.member.Member;
import me.seongho9.dev.domain.member.dto.InfoDTO;
import me.seongho9.dev.domain.member.dto.ModifyDTO;
import me.seongho9.dev.domain.member.dto.SignupDTO;
import me.seongho9.dev.domain.member.vo.InfoVO;
import me.seongho9.dev.repository.MemberRepository;
import me.seongho9.dev.repository.PortRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sound.sampled.Port;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PortRepository portRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void afterEach() {
        memberRepository.deleteAll();
        portRepository.deleteAll();
    }
    @Test
    void signup() {
        //given
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setName("seongho_jang");
        signupDTO.setPasswd("1234");
        signupDTO.setUserId("seongho9");
        signupDTO.setMail("seongho9@gmail.com");
        //when
        String id = memberService.signup(signupDTO);
        //then
        Member member = memberRepository.findById("seongho9").get();
        Assertions.assertThat(id).isEqualTo(member.getId());
    }

    @Test
    void signupElementIsNotValid(){
        //given
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setName("seongho_jang");
        signupDTO.setPasswd("1234");
        signupDTO.setUserId("seongho9");
        signupDTO.setMail(null);
        //when-then
        Assertions.assertThatThrownBy(()->memberService.signup(signupDTO))
                .isInstanceOf(JpaSystemException.class);
    }

    @Test
    void modifyMember() {
        //given
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setName("seongho_jang");
        signupDTO.setPasswd("1234");
        signupDTO.setUserId("seongho9");
        signupDTO.setMail("seongho9@gmail.com");
        memberService.signup(signupDTO);

        ModifyDTO modifyDTO = new ModifyDTO();
        modifyDTO.setUserId("seongho9");
        modifyDTO.setPasswdBefore("1234");
        modifyDTO.setPasswdAfter("5678");
        modifyDTO.setMail("seongho9@gmail.com");
        //when
        memberService.modifyMember(modifyDTO);
        //then
        Member member = memberRepository.findById("seongho9").get();
        Assertions.assertThat(
                passwordEncoder.matches("5678", member.getPasswd())
        ).isEqualTo(true);

    }

    @Test
    void memberInfo() {
        //given
        //member signup
        SignupDTO signupDTO = new SignupDTO();
        signupDTO.setName("seongho_jang");
        signupDTO.setPasswd("1234");
        signupDTO.setUserId("seongho9");
        signupDTO.setMail("seongho9@gmail.com");
        String id = memberService.signup(signupDTO);

        InfoDTO infoDTO = new InfoDTO();
        infoDTO.setUserId("seongho9");
        //when
        InfoVO member = memberService.memberInfo(infoDTO);
        //then
        Assertions.assertThat(member.getId()).isEqualTo(infoDTO.getUserId());
    }

}
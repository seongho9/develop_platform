package me.seongho9.dev.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.domain.member.dto.InfoDTO;
import me.seongho9.dev.domain.member.dto.ModifyDTO;
import me.seongho9.dev.domain.member.dto.SignupDTO;
import me.seongho9.dev.domain.member.vo.InfoVO;
import me.seongho9.dev.service.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private final MemberService memberService;

    @PostMapping("/signup")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public String signup(@RequestBody SignupDTO signupDTO) {

        String id = memberService.signup(signupDTO);

        return "request for manager";
    }

    @GetMapping("/info")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public InfoVO getMemberInfo(@RequestBody InfoDTO infoDTO) {
        log.info("member info");
        InfoVO member = memberService.memberInfo(infoDTO);

        return member;
    }

    @PostMapping("/modify")
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public String modifyMember(@RequestBody ModifyDTO modifyDTO) {

        String id = memberService.modifyMember(modifyDTO);

        return id + " modified success";
    }
}

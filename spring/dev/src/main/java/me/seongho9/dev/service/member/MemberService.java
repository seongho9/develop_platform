package me.seongho9.dev.service.member;

import me.seongho9.dev.domain.member.dto.InfoDTO;
import me.seongho9.dev.domain.member.Member;
import me.seongho9.dev.domain.member.dto.ModifyDTO;
import me.seongho9.dev.domain.member.dto.SignupDTO;
import me.seongho9.dev.domain.member.vo.InfoVO;

public interface MemberService {

    String signup(SignupDTO signupDTO);

    String modifyMember(ModifyDTO modifyDTO);

    InfoVO memberInfo(InfoDTO infoDTO);

    String logout(String userId);

}
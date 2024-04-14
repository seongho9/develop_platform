package me.seongho9.dev.service.member;

import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.domain.ExposePorts;
import me.seongho9.dev.domain.member.Member;
import me.seongho9.dev.domain.member.dto.InfoDTO;
import me.seongho9.dev.domain.member.dto.ModifyDTO;
import me.seongho9.dev.domain.member.dto.SignupDTO;
import me.seongho9.dev.domain.member.vo.InfoVO;
import me.seongho9.dev.excepction.member.MemberExistException;
import me.seongho9.dev.excepction.member.MemberNotFoundException;
import me.seongho9.dev.excepction.member.PasswordNotMatchException;
import me.seongho9.dev.mapper.MemberMapper;
import me.seongho9.dev.repository.MemberRepository;
import me.seongho9.dev.repository.PortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PortRepository portRepository;
    @Autowired
    MemberMapper memberMapper;

    @Override
    @Transactional
    public String signup(SignupDTO signupDTO) {

        try {
            if(portRepository.findEndOfPorts().isEmpty()){
                ExposePorts ports = new ExposePorts();
                ports.setStart(9000);
                ports.setCurrent(9000);
                ports.setEnd(9000);
                portRepository.save(ports);
            }
            Integer endOfPorts = portRepository.findEndOfPorts().get();
            ExposePorts ports = new ExposePorts();
            ports.setStart(endOfPorts + 1);
            ports.setEnd(endOfPorts + 10);
            ports.setCurrent(endOfPorts + 1);
            portRepository.save(ports);
            log.info("{}, {}", signupDTO.getUserId(), signupDTO.getPasswd());
            log.info("Ports allocated {}-to-{}", ports.getStart(), ports.getEnd());
            signupDTO.setPasswd(
                    passwordEncoder.encode(signupDTO.getPasswd())
            );
            Member member = memberMapper.signupDTOToMember(signupDTO);
            member.setPorts(ports);

            memberRepository.save(member);

            return member.getId();
        } catch (EntityExistsException e) {
            throw new MemberExistException(e);
        }
    }

    @Override
    @Transactional
    public String modifyMember(ModifyDTO modifyDTO) {

        Optional<Member> optional = memberRepository.findById(modifyDTO.getUserId());
        if(optional.isEmpty()){
            throw new MemberNotFoundException("userId " + modifyDTO.getUserId() + " is not exist");
        }
        if(!passwordEncoder.matches(modifyDTO.getPasswdBefore(), optional.get().getPasswd())){
            throw new PasswordNotMatchException("password not match");
        }

        Member member = optional.get();
        member.setPasswd(passwordEncoder.encode(modifyDTO.getPasswdAfter()));
        member.setMail(modifyDTO.getMail());

        Member save = memberRepository.save(member);

        return save.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public InfoVO memberInfo(InfoDTO infoDTO) {

        Optional<Member> optional = memberRepository.findById(infoDTO.getUserId());
        if(optional.isEmpty()){
            throw new MemberNotFoundException("userId " + infoDTO.getUserId() + " is not exist");
        }
        Member member = optional.get();
        InfoVO infoVO = InfoVO.builder()
                .id(member.getId())
                .mail(member.getMail())
                .userName(member.getUserName())
                .build();
        log.info("{}", infoVO.getId());
        return infoVO;

    }

    @Override
    public String logout(String userId) {

        memberRepository.destroyRefreshToken(userId);

        return null;
    }
}

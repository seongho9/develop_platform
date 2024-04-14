package me.seongho9.dev.mapper;

import javax.annotation.processing.Generated;
import me.seongho9.dev.domain.member.Member;
import me.seongho9.dev.domain.member.dto.ModifyDTO;
import me.seongho9.dev.domain.member.dto.SignupDTO;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-03-31T14:47:01+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17 (Oracle Corporation)"
)
@Component
public class MemberMapperImpl implements MemberMapper {

    @Override
    public Member signupDTOToMember(SignupDTO signupDTO) {
        if ( signupDTO == null ) {
            return null;
        }

        Member member = new Member();

        member.setId( signupDTO.getUserId() );
        member.setPasswd( signupDTO.getPasswd() );
        member.setUserName( signupDTO.getName() );
        member.setMail( signupDTO.getMail() );

        return member;
    }

    @Override
    public Member ModifyDTOToMember(ModifyDTO modifyDTO) {
        if ( modifyDTO == null ) {
            return null;
        }

        Member member = new Member();

        member.setId( modifyDTO.getUserId() );
        member.setPasswd( modifyDTO.getPasswdAfter() );
        member.setMail( modifyDTO.getMail() );

        return member;
    }
}

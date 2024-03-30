package me.seongho9.dev.mapper;

import me.seongho9.dev.domain.member.Member;
import me.seongho9.dev.domain.member.dto.ModifyDTO;
import me.seongho9.dev.domain.member.dto.SignupDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "passwd", source = "passwd")
    @Mapping(target = "userName", source = "name")
    @Mapping(target = "mail", source = "mail")
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "ports", ignore = true)
    Member signupDTOToMember(SignupDTO signupDTO);

    @Mapping(target = "id", source = "userId")
    @Mapping(target = "passwd", source = "passwdAfter")
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "mail", source = "mail")
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "ports", ignore = true)
    Member ModifyDTOToMember(ModifyDTO modifyDTO);


}

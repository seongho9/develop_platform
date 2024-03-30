package me.seongho9.dev.repository;

import me.seongho9.dev.domain.ExposePorts;
import me.seongho9.dev.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PortRepository portRepository;

    @BeforeEach
    void beforeEach(){
        memberRepository.deleteAll();
        portRepository.deleteAll();
    }

    @Test
    void saveMember(){
        ExposePorts ports = new ExposePorts();
        ports.setStart(1);
        ports.setEnd(2);
        ports.setCurrent(1);
        Member member = new Member("1", "1", "1", "1", "", ports);
        portRepository.save(ports);
        memberRepository.save(member);
    }
    @Test
    void saveMemberPortsNull(){
        //given
        Member member = new Member("1", "1", "1", "1", "");
        //then
        Assertions.assertThatThrownBy(()->memberRepository.save(member))
                .isInstanceOf(JpaSystemException.class);
    }

    @Test
    void saveMemberDuplicate() {
        //JpaRepository .save 는 일반 SQL처럼 INSERT로만 작용하는게 아니고, 이미 있으면 변경 부분만 바꾸는 UPDATE로 작용
    }
    @Test
    void findPasswordById() {
        //given
        ExposePorts ports = new ExposePorts();
        ports.setStart(1);
        ports.setEnd(2);
        ports.setCurrent(1);
        Member member = new Member("1", "1", "1", "1", "", ports);
        portRepository.save(ports);
        memberRepository.save(member);
        //when
        String id = memberRepository.findPasswordById("1");
        //then
        Assertions.assertThat(id).isEqualTo(member.getId());
    }
    @Test
    void getPorts(){
        //given
        ExposePorts ports = new ExposePorts();
        ports.setStart(1);
        ports.setEnd(2);
        ports.setCurrent(1);
        Member member = new Member("1", "1", "1", "1", "", ports);
        portRepository.save(ports);
        memberRepository.save(member);
        //when
        Member member1 = memberRepository.findById("1").get();
        //then
        Assertions.assertThat(member1.getPorts().getStart()).isEqualTo(1);

    }

}
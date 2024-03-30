package me.seongho9.dev.repository;

import me.seongho9.dev.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    @Query(
            nativeQuery = true,
            value="SELECT password FROM member where id=:id"
    )
    String findPasswordById(@Param("id") String id);

    @Query(
            nativeQuery = true,
            value="SELECT * FROM member where refresh=:refresh"
    )
    Optional<Member> findByRefreshToken(@Param("refresh") String refresh);
    @Query(
            nativeQuery = true,
            value = "UPDATE member SET refresh='' WHERE id=:id"
    )
    @Modifying
    @Transactional
    Integer destroyRefreshToken(@Param("id") String id);

    @Query(
            nativeQuery = true,
            value="UPDATE member set refresh=:refresh WHERE id=:id"
    )
    @Modifying
    @Transactional
    Integer updateRefreshToken(@Param("id")String id, @Param("refresh") String refreshToken);
}

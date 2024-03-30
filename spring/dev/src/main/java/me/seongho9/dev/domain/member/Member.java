package me.seongho9.dev.domain.member;

import jakarta.persistence.*;
import lombok.*;
import me.seongho9.dev.domain.ExposePorts;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "member")
public class Member {
    @Id
    @Column(name="id", length = 20)
    private String  id;
    @Column(name="password", nullable = false)
    private String passwd;
    @Column(name = "user_name", length = 20,nullable = false)
    private String userName;
    @Column(name="mail", nullable = false)
    private String mail;
    @Column(name="refresh", nullable = true)
    private String refreshToken;

    @OneToOne
    @JoinColumn(name="port_id",nullable = false)
    private ExposePorts ports;

    public Member(String id, String passwd, String userName, String mail, String refresh) {
        this.id = id;
        this.passwd = passwd;
        this.userName = userName;
        this.mail = mail;
        this.refreshToken = refresh;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void destroyRefreshToken(){
        this.refreshToken = null;
    }
}
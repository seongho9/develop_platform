package me.seongho9.dev.service.security;

import lombok.RequiredArgsConstructor;
import me.seongho9.dev.domain.member.Member;
import me.seongho9.dev.domain.member.MemberDetailsImpl;
import me.seongho9.dev.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> optional = memberRepository.findById(username);
        if (optional.isEmpty()) {
            throw new NoSuchElementException("userId " + username + " is not exist");
        }
        return new MemberDetailsImpl(optional.get());
    }
}

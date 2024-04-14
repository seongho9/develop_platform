package me.seongho9.dev.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.domain.member.Member;
import me.seongho9.dev.domain.member.MemberDetailsImpl;
import me.seongho9.dev.repository.MemberRepository;
import me.seongho9.dev.service.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final MemberRepository memberRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private final String LOGIN_URL = "/member/login";
    private final String SIGNUP_URL = "/member/signup";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(LOGIN_URL) || request.getRequestURI().equals(SIGNUP_URL)) {
            filterChain.doFilter(request, response);
            return;
        }
        String refresh = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);
        if (refresh != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refresh);
            return;
        }
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }
    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(
                accessToken -> jwtService.extractId(accessToken).ifPresent(
                        id -> memberRepository.findById(id).ifPresent((
                                member -> saveAuthentication(member))
                        )
                ));
        log.info("token {}", jwtService.extractAccessToken(request));
        filterChain.doFilter(request,response);
    }

    private void saveAuthentication(Member member) {
        MemberDetailsImpl details = new MemberDetailsImpl(member);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(details, null, authoritiesMapper.mapAuthorities(details.getAuthorities()));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refresh) {
        //Member member = memberRepository.findByRefreshToken(refresh);
        memberRepository.findByRefreshToken(refresh).ifPresent(
                (member)-> jwtService.sendAccessToken(
                        response, jwtService.createAccessToken(member.getId()))
        );
    }
}

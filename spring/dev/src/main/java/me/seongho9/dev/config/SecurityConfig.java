package me.seongho9.dev.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seongho9.dev.repository.MemberRepository;
import me.seongho9.dev.security.filter.JsonUsernamePasswordAuthenticationFilter;
import me.seongho9.dev.security.filter.JwtAuthenticationProcessingFilter;
import me.seongho9.dev.security.handler.LoginFailureHandler;
import me.seongho9.dev.security.handler.LoginSuccessHandler;
import me.seongho9.dev.service.security.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private static final String[] URL_TO_PERMIT = {
            "/member/login",
            "/member/signup",
    };
    //@Bean
    CorsConfigurationSource corsConfigurationSource() {
        log.info("cors");
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        corsConfiguration.setAllowedOriginPatterns(Collections.singletonList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE","PUT"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return source;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http    .httpBasic(HttpBasicConfigurer::disable)
                //.cors(cors->cors.disable())
                .csrf(AbstractHttpConfigurer::disable);

        http    .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers(URL_TO_PERMIT).permitAll()
                        .anyRequest().authenticated())
                .logout((logout) -> logout
                        //delete total session after logout
                        .invalidateHttpSession(true))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http
                .logout((logout)-> logout
                        .logoutUrl("/member/logout")
                        .addLogoutHandler((request, response, authentication) -> {
                            jwtService.extractAccessToken(request).filter(jwtService::isTokenValid).ifPresent(
                                    accessToken -> jwtService.extractId(accessToken).ifPresent(
                                            id -> memberRepository.destroyRefreshToken(id)
                                    ));
                            authentication.setAuthenticated(false);
                        }))
                .addFilterAfter(jsonUsernamePasswordAuthFilter(), LogoutFilter.class)
                .addFilterBefore(jwtAuthorizationProcessingFilter(), JsonUsernamePasswordAuthenticationFilter.class);

        log.info("security config");
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        //bCrypt
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    //passwordEncoder & userDetailsService 등록
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }
    //Provider Manager로 감쌈
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        DaoAuthenticationProvider provider = daoAuthenticationProvider();
        return new ProviderManager(provider);
    }

    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthFilter() throws Exception{
        JsonUsernamePasswordAuthenticationFilter loginFilter = new JsonUsernamePasswordAuthenticationFilter(objectMapper);

        loginFilter.setAuthenticationManager(authenticationManager());

        loginFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(jwtService, memberRepository));
        loginFilter.setAuthenticationFailureHandler(new LoginFailureHandler());
        return loginFilter;
    }
    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthorizationProcessingFilter(){
        JwtAuthenticationProcessingFilter filter = new JwtAuthenticationProcessingFilter(jwtService, memberRepository);

        return filter;
    }
}

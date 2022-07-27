package com.back.moyeomoyeo.security;

import com.back.moyeomoyeo.service.login.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailServiceImpl userDetailService;
    private final BCryptPasswordEncoder passwordEncoder;
    private  final PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.formLogin()
                .loginPage("/login").defaultSuccessUrl("/", true)
                .loginProcessingUrl("/login").defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm").defaultSuccessUrl("/")
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

        http.logout().logoutUrl("/logout").logoutSuccessUrl("/");

        //로그인 페이지
        http.formLogin().loginPage("/login").defaultSuccessUrl("/", true);
        //로그인 요청시
        http.formLogin().loginProcessingUrl("/login").defaultSuccessUrl("/", true);
        // 로그아웃
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/");

    }
}

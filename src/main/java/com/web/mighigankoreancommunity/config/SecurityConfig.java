package com.web.mighigankoreancommunity.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults()) // csrf activate
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/page/member/login").permitAll()
                        .requestMatchers("/api/member/checkEmail","/page/member/login", "/page/member/register", "/page/member/me"
                                ,"/page/inventory/goInventory"
                                ,"/page/restaurant/list", "/api/restaurant/save"
                                ,"/css/**", "/js/**").
                        permitAll().anyRequest().authenticated()
                )



                .formLogin(form -> form
//                       login page url
                        .loginPage("/page/member/login")
//                        after login success
                        .loginProcessingUrl("/page/member/login")
//                        input name "memberEmail, memberPassword" automatically
                        .usernameParameter("memberEmail")
                        .passwordParameter("memberPassword")
                        .defaultSuccessUrl("/page/restaurant/list", true)
                                .failureUrl("/page/member/login?error=true")
                        .permitAll()
                );

        return http.build();
    }

}

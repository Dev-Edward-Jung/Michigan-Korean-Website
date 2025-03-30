package com.web.mighigankoreancommunity.config;


import com.web.mighigankoreancommunity.service.owner.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(Customizer.withDefaults()) // csrf activate
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()   // 모든 GET 허용 (선택 사항)
                        .requestMatchers(HttpMethod.POST, "/**").permitAll()  // ✅ 모든 POST 허용
                        .requestMatchers(HttpMethod.PUT, "/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/**").permitAll()
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/fonts/**",
                                "/vendor/**",
                                "/favicon.ico",
                                "/page/error/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .rememberMe(rememberMe -> rememberMe
                        .key("secure-remember-me-key")
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(60 * 60 * 24 * 14)
                        .userDetailsService(customUserDetailsService)
                )



                .formLogin(form -> form
//                       login page url
                        .loginPage("/page/owner/login")
//                        after login success
                        .loginProcessingUrl("/page/owner/login")
//                        input name "memberEmail, memberPassword" automatically
                        .usernameParameter("ownerEmail")
                        .passwordParameter("ownerPassword")
                        .defaultSuccessUrl("/page/restaurant/list", true)
                                .failureUrl("/page/owner/login?error=true")
                        .permitAll()
                );

        return http.build();
    }

}

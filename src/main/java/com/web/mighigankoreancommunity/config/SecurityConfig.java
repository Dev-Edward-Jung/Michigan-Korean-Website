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
                        .requestMatchers(
                                "/css/**",
                                "/js/**",
                                "/img/**",
                                "/fonts/**",
                                "/favicon.ico",
                                "/page/error/**",
                                "/api/restaurant/list","/api/restaurant/save",
                                "/page/restaurant/list",
                                "/page/owner/register", "/page/owner/login", "/page/owner/logout", "/page/owner/forgot/password", "/api/owner/me", "/api/owner/checkEmail",
                                "/page/inventory/list",
                                "/api/inventory/list", "/api/inventory/save", "/api/inventory/unit/", "/api/inventory/unit/list", "/api/inventory/delete", "/api/inventory/update",
                                "/page/category/list",
                                "/api/category/list", "/api/category/save", "/api/category/delete", "/api/category/update",
                                "/page/employee/invited",
                                "/page/announcement/list"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                .rememberMe(rememberMe -> rememberMe
                        .key("secure-remember-me-key")
                        .rememberMeParameter("remember-me")
                        .tokenValiditySeconds(60 * 60 * 24 * 14)
                        .userDetailsService(customUserDetailsService)
                )

                .logout(logout -> logout
                        .logoutUrl("/page/owner/logout")
                        .logoutSuccessUrl("/page/owner/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                        .permitAll()
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

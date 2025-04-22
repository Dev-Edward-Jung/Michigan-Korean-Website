package com.web.mighigankoreancommunity.config;


import com.web.mighigankoreancommunity.service.owner.OwnerUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Order(1)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class OwnerSecurityConfig {

    private final OwnerUserDetailsService ownerDetailsService;



    @Bean
    public SecurityFilterChain ownerSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .securityMatcher("/page/owner/**", "/api/owner/**",
                        "/page/restaurant/**", "/api/restaurant/**",
                        "/page/employee/**", "/api/employee/**",
                        "/page/inventory/**", "/api/inventory/**",
                        "/page/announcement/**", "/api/announcement/**",
                        "/page/category/**", "/api/category/**") // owner 관련 URL만 필터됨
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/page/owner/register",
                                "/page/owner/login",
                                "/page/owner/forgot/password",
                                "/api/owner/checkEmail",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/page/owner/login")
                        .loginProcessingUrl("/page/owner/login")
                        .usernameParameter("ownerEmail")
                        .passwordParameter("ownerPassword")
                        .defaultSuccessUrl("/page/restaurant/list", true)
                        .failureUrl("/page/owner/login?error=true")
                        .permitAll()
                )
                .userDetailsService(ownerDetailsService)
                .logout(logout -> logout
                        .logoutUrl("/page/owner/logout")
                        .logoutSuccessUrl("/page/owner/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                )
                .rememberMe(r -> r
                        .key("secure-owner-key")
                        .rememberMeParameter("remember-me")
                        .userDetailsService(ownerDetailsService)
                )
                .csrf(Customizer.withDefaults());

        return http.build();
    }

}

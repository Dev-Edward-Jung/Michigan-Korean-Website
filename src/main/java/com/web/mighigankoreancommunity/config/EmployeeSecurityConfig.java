package com.web.mighigankoreancommunity.config;


import com.web.mighigankoreancommunity.service.employee.EmployeeUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;


@Order(2)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class EmployeeSecurityConfig {

    private final EmployeeUserDetailsService employeeDetailsService;

    @Bean
    public SecurityFilterChain employeeSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()))
                .securityMatcher("/page/employee/**", "/api/employee/**",
                        "/page/restaurant/**", "/api/restaurant/**",
                        "/page/announcement/**", "/api/announcement/**"
                        ) // employee 관련 요청만 필터됨
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/page/employee/login", "/page/employee/invited").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/page/employee/login")
                        .loginProcessingUrl("/page/employee/login")
                        .usernameParameter("employeeEmail")
                        .passwordParameter("employeePassword")
                        .defaultSuccessUrl("/page/schedule/list", true)
                        .failureUrl("/page/employee/login?error=true")
                        .permitAll()
                )
                .userDetailsService(employeeDetailsService)
                .logout(logout -> logout
                        .logoutUrl("/page/employee/logout")
                        .logoutSuccessUrl("/page/employee/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "remember-me")
                )
                .rememberMe(r -> r
                        .key("secure-employee-key")
                        .rememberMeParameter("remember-me")
                        .userDetailsService(employeeDetailsService)
                )
                .csrf(Customizer.withDefaults());

        return http.build();
    }

}

package com.web.mighigankoreancommunity.config;

import com.web.mighigankoreancommunity.service.owner.OwnerUserDetailsService;
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

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final OwnerUserDetailsService ownerDetailsService;
    private final EmployeeUserDetailsService employeeDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
        handler.setCsrfRequestAttributeName("_csrf"); // Thymeleaf에서 사용 가능하도록 설정

        http
                .csrf(csrf -> csrf.csrfTokenRequestHandler(handler))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain ownerSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
        handler.setCsrfRequestAttributeName("_csrf");

        http
                .securityMatcher("/page/owner/**", "/api/owner/**")
                .csrf(csrf -> csrf.csrfTokenRequestHandler(handler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/page/owner/login", "/page/owner/register", "/page/owner/forgot/password",
                                "/page/restaurant/**",
                                "/api/restaurant/**",
                                "/page/inventory/**",
                                "/page/category/**",
                                "/page/announcement/**",
                                "/api/owner/checkEmail", "/error"
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

    @Bean
    @Order(2)
    public SecurityFilterChain employeeSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
        handler.setCsrfRequestAttributeName("_csrf");

        http
                .securityMatcher("/page/employee/**", "/api/employee/**")
                .csrf(csrf -> csrf.csrfTokenRequestHandler(handler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/page/employee/login", "/page/employee/invited", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/page/employee/login")
                        .loginProcessingUrl("/page/employee/login")
                        .usernameParameter("employeeEmail")
                        .passwordParameter("employeePassword")
                        .defaultSuccessUrl("/page/restaurant/list", true)
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

    @Bean
    @Order(100)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(
                        "/css/**", "/js/**", "/img/**", "/favicon.ico",
                        "/error", "/",
                        "/page/owner/login", "/page/owner/register",
                        "/page/employee/login", "/page/employee/register",
                        "/page/employee/invited"
                )
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .csrf(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
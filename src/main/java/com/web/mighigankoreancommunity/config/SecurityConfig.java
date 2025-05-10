package com.web.mighigankoreancommunity.config;

import com.web.mighigankoreancommunity.service.owner.OwnerUserDetailsService;
import com.web.mighigankoreancommunity.service.employee.EmployeeUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import java.util.List;

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
                                "/page/owner/login", "/page/owner/register", "/page/owner/forgot/password", "/api/owner/forgot/password",
                                "/page/restaurant/**",
                                "/api/restaurant/**",
                                "/page/inventory/**",
                                "/page/category/**",
                                "/page/announcement/**",
                                "/api/owner/checkEmail", "/error", "/page/owner/reset/password", "/api/owner/reset/password"
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
                        .requestMatchers("/page/employee/login", "/page/employee/invited", "/error",
                                "/api/employee/forgot/password", "/page/employee/forgot/password",
                                "/page/employee/reset/password",
                                "/page/restaurant/**, /api/restaurant/**").permitAll()
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
        CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
        handler.setCsrfRequestAttributeName("_csrf");

        http
                .cors(cors-> cors
                        .configurationSource((request ->{
                            var configuration = new org.springframework.web.cors.CorsConfiguration();
                            configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Next.js 주소
                            configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            configuration.setAllowedHeaders(List.of("*"));
                            configuration.setAllowCredentials(true);
                            return configuration;
                        })))
                .addFilterAfter(new CsrfCookieFilter(), CsrfFilter.class)
                .csrf(csrf -> csrf.csrfTokenRequestHandler(handler))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**", "/js/**", "/img/**", "/favicon.ico", "/error/**", "/"
                        ).permitAll()
                        .requestMatchers(
                                "/page/owner/login", "/page/owner/register", "/page/owner/forgot/password",
                                "/page/employee/login", "/page/employee/register", "/page/employee/invited",
                                "/api/owner/forgot/password", "/api/employee/forgot/password", "/page/employee/forgot/password",
                                "/page/owner/reset/password", "/page/employee/reset/password",
                                "/page/announcement/**", "/api/announcement/**", "/page/inventory/**", "/api/inventory/**",
                                "/api/owner/reset/password", "/api/employee/reset/password",
                                "/csrf"
                        ).permitAll()
                        .anyRequest().authenticated()
                )

                // ❌ 기본 formLogin() 제거
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            // 인증 안 된 경우 리다이렉트
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\"}");
                        })
                );


        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
}
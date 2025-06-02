package com.web.mighigankoreancommunity.config;

import com.web.mighigankoreancommunity.jwt.JwtAuthenticationFilter;
import com.web.mighigankoreancommunity.service.employee.EmployeeUserDetailsService;
import com.web.mighigankoreancommunity.service.owner.OwnerUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OwnerUserDetailsService ownerUserDetailsService;
    private final EmployeeUserDetailsService employeeUserDetailsService;

    @Bean
    public DaoAuthenticationProvider ownerAuthProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(ownerUserDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        p.setHideUserNotFoundExceptions(false); // 💡 핵심 설정
        return p;
    }

    @Bean
    public DaoAuthenticationProvider employeeAuthProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(employeeUserDetailsService);
        p.setPasswordEncoder(passwordEncoder());
        p.setHideUserNotFoundExceptions(false); // 💡 핵심 설정
        return p;
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder
                .authenticationProvider(ownerAuthProvider())
                .authenticationProvider(employeeAuthProvider());

        return authBuilder.build();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1) Disable CSRF entirely (since you’re using JWTs + stateless)
                .csrf(csrf -> csrf.disable())

                // 2) CORS if you need it
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 3) Stateless session (no HTTP session)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5) Public endpoints first
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // 🔥 이 줄 추가!
                        .requestMatchers("/auth/login/**", "/auth/register/**",
                                "/auth/forget/password", "/auth/reset/password",
                                "/api/employee/forgot/**", "/api/employee/register",
                                "/api/owner/checkEmail"
                        ).permitAll()
                        .requestMatchers(
                                "/api/restaurant/**", "/api/inventory/**", "/api/category/**",
                                "/api/employee/schedule/**","/api/employee/list","/api/employee/invite","/api/employee/schedule/save",
                                "/api/announcement/**"
                                ).authenticated()
                        .anyRequest().authenticated()
                )

                        // any other request also requires auth

                // 6) Insert your JWT filter before the username/password filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // 7) On auth-failure return 401 JSON
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            res.setContentType("application/json");
                            res.getWriter().write("{\"error\":\"Unauthorized\"}");
                        })
                )
        ;

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * CORS 정책:
     * - 허용할 오리진: http://localhost:3000
     * - 자격증명 포함(쿠키/헤더)
     * - CSRF 헤더(X-XSRF-TOKEN)와 Authorization 헤더 허용
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of(
                "Content-Type",
                "X-XSRF-TOKEN",
                "Authorization"
        ));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }

}
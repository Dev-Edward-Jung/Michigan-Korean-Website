package com.web.mighigankoreancommunity.jwt;

import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.service.employee.EmployeeUserDetailsService;
import com.web.mighigankoreancommunity.service.owner.OwnerUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final OwnerUserDetailsService ownerUserDetailsService;
    private final EmployeeUserDetailsService employeeUserDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest req) {
        String path = req.getServletPath();
        return path.startsWith("/auth/login")
                || path.startsWith("/auth/register")
                || path.equals("/csrf");
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println(request.getMethod());
        // 🔥 OPTIONS 요청은 바로 통과
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }


        String token = jwtTokenProvider.resolveToken(request);
//        System.out.println("Inside JwtAuthFilter, token is  : :" +  token);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            String email = jwtTokenProvider.getEmail(token);
            MemberRole role = jwtTokenProvider.getRole(token); // JWT에서 꺼낸 사용자 역할
            System.out.println("Inside JwtAuthFilter, role is  : :" +  role);
            System.out.println("Inside JwtAuthFilter, Email is  : :" +  email);
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = null;

            if ("OWNER".equals(role)) {
                userDetails = ownerUserDetailsService.loadUserByUsername(email);
            } else if ("EMPLOYEE".equals(role)) {
                userDetails = employeeUserDetailsService.loadUserByUsername(email);
            }

            if (userDetails != null) {
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }
}

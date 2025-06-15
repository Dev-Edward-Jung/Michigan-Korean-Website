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

import javax.management.RuntimeErrorException;
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
        return path.startsWith("/api/auth/login")
                || path.startsWith("/api/auth/register")
                || path.startsWith("/api/auth/checkEmail")   // ✅ 이 줄 추가!
                || path.startsWith("/api/owner/checkEmail")   // ← 혹시 Owner용도 예외 추가 필요하면
                || path.startsWith("/api/auth/forget")        // 기타 허용하려는 경로 추가
                || path.equals("/api/auth/validate");         // ✅ GET /validate 같은 것도 예외
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
            Long id = jwtTokenProvider.getId(token);
            System.out.println("Inside JwtAuthFilter, role is  : :" +  role);
            System.out.println("Inside JwtAuthFilter, Email is  : :" +  email);
            System.out.println("Inside JwtAuthFilter, id is  : :" +  id);
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = null;

            if (MemberRole.OWNER.equals(role)) {
                userDetails = ownerUserDetailsService.loadUserByUsername(email);
            } else if (MemberRole.EMPLOYEE.equals(role) || MemberRole.KITCHEN.equals(role)
                    || MemberRole.MANAGER.equals(role) || MemberRole.SERVER.equals(role)) {
                userDetails = employeeUserDetailsService.loadUserByUsername(email);
            } else{
                throw new RuntimeException("Invalid role or not exist");
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

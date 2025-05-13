package com.web.mighigankoreancommunity.jwt;

import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.service.employee.EmployeeUserDetailsService;
import com.web.mighigankoreancommunity.service.owner.OwnerService;
import com.web.mighigankoreancommunity.service.owner.OwnerUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key;
    private final OwnerUserDetailsService ownerUserDetailsService;
    private final EmployeeUserDetailsService employeeUserDetailsService;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * 토큰 생성
     */
    public String createToken(String userEmail, MemberRole role) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", role.name());

        Date now = new Date();
        Date validity = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰에서 이메일(subject) 추출
     */
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 토큰에서 역할(role) 추출
     */
    public MemberRole getRole(String token) {
        String role = (String) getClaims(token).get("role");
        return MemberRole.valueOf(role);
    }

    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * HTTP 요청 헤더에서 Bearer 토큰 추출
     */
    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    /**
     * 토큰에서 Claim 정보 파싱
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰으로부터 Authentication 객체 생성
     */
    // Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        String email = getEmail(token);
        UserDetails userDetails;
        if (ownerUserDetailsService.existsByEmail(email)) {
            userDetails = ownerUserDetailsService.loadUserByUsername(email);
        } else {
            userDetails = ownerUserDetailsService.loadUserByUsername(email);
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}

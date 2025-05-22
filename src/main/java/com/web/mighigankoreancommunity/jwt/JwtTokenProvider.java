package com.web.mighigankoreancommunity.jwt;

import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.service.employee.EmployeeUserDetailsService;
import com.web.mighigankoreancommunity.service.owner.OwnerService;
import com.web.mighigankoreancommunity.service.owner.OwnerUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
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
        // plain-text secret 사용 시, UTF-8 바이트로 키 생성
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 토큰 생성
     */
    public String createToken(String userEmail, MemberRole role, Long id) {
        Claims claims = Jwts.claims().setSubject(userEmail);
        claims.put("role", role.name());
        claims.put("id", id); // 🔥 추가

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
     * 토큰에서 id 추출
     */
    public Long getId(String token) {
        Object idClaim = getClaims(token).get("id");
        System.out.println(idClaim);
        if (idClaim instanceof Number) {
            return ((Number) idClaim).longValue(); // Long으로 안전하게 변환
        }
        throw new IllegalArgumentException("Invalid or missing ID in token");
    }




    /**
     * 토큰 유효성 검증
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)                     // ← key 객체로 검증
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원하지 않는 JWT 토큰", e);
        } catch (MalformedJwtException e) {
            log.error("유효하지 않은 JWT 토큰 형식", e);
        } catch (SignatureException e) {
            log.error("잘못된 JWT 서명", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 비어 있거나 잘못된 인수", e);
        }
        return false;
    }

    /**
     * HTTP 요청 헤더에서 Bearer 토큰 추출
     */
    public String resolveToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            String token = header.substring(7).trim();
            if (StringUtils.hasText(token)) {
                System.out.println("들어온 JWT: " + token);
                return token;
            }
        }
        return null;  // 없거나 빈 토큰은 null 처리
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
        } else if(employeeUserDetailsService.existsByEmail(email)) {
            userDetails = employeeUserDetailsService.loadUserByUsername(email);  // 수정: 직원 서비스 사용
        } else {
            throw new RuntimeException("No user found with email: " + email);
        }

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }
}

package com.example.shoppingmall.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
// 발급받은 토큰 사용자에게 전달
// SecurityContext에 사용자 정보를 등록하는 역할,
// 로그인 이후에 클라이언트가 JWT 토큰을 이용하여 자원에 접근할 때 사용됩니다.
// 사용자를 어떻게 인증을 할 것인가 하는 인증을 하기 위해 필터를 만든다.
public class JwtTokenFilter extends OncePerRequestFilter {
    // OncePerRequestFilter: 각 HTTP 요청에 대해 딱 한 번만 실행
    // 필터 체인을 통과하는 동안 필터가 중복으로 실행되는 것을 방지하고, 순서 보장

    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsManager manager;



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("token in Header");
        String authHeader
                = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info(authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.split(" ")[1];
            log.info(token);
            // 정당한 토큰인지 검사
            if (jwtTokenUtils.validate(token)) {
                // 정당하면 인증정보 객체에 담아서 인증 받은 사용자임을 확인
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                // 사용자 정보 회수
                String username = jwtTokenUtils
                        .parseClaims(token)
                        .getSubject();

                UserDetails userDetails = manager.loadUserByUsername(username);
                for (GrantedAuthority authority : userDetails.getAuthorities()) {
                    log.info("authority: {}", authority.getAuthority());
                }

                // 인증 정보 생성 및 권한 주입
                AbstractAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        token,
                        userDetails.getAuthorities()
                );

                // 인증 정보 등록
                log.info("jwt 보안 인증 등록");
                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);
            } else {
                log.info("정당한 jwt가 아닙니다.");
            }
        }
        filterChain.doFilter(request, response);
    }


}

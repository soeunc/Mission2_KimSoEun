package com.example.shoppingmall;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class LogFilter implements Filter {
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        log.info("start request: {} {}",
                httpServletRequest.getMethod(),
                httpServletRequest.getRequestURI()
        );
        // request를 다른 객체로 바꿔서 호출할 수 있다.(읽고 넘어가면 됨)
        // 한번이라도 읽기 내역이 있었으면 그 내역을 저장해주는 것
        ContentCachingRequestWrapper requestWrapper
                = new ContentCachingRequestWrapper(httpServletRequest);

        // 요청을 보내기 전에는 body가 읽히지 않는다.
        log.info(new String(
                requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8));

        // doFilter를 호출하지 않을 경우 다음 필터가 실행되지 않으며
        // -> 요청이 끝까지 전달되지 않는다.
        // --- 이 위는 요청 처리 전
        chain.doFilter(requestWrapper, response);
        // --- 이 아래는 요청 처리 후

        // JSON에서 보낸 body가 읽힌다.
        log.info(new String(
                requestWrapper.getContentAsByteArray(), StandardCharsets.UTF_8));

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        log.info("send response: {}", httpServletResponse.getStatus());
    }
}

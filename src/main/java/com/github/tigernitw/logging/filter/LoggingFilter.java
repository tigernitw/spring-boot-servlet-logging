package com.github.tigernitw.logging.filter;

import com.github.tigernitw.logging.config.LoggingAutoConfig;
import com.github.tigernitw.logging.servlet.request.CachedHttpServletRequest;
import com.github.tigernitw.logging.servlet.response.CachedHttpServletResponse;
import com.github.tigernitw.logging.util.MDCUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component()
@EqualsAndHashCode(callSuper = true)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingFilter extends OncePerRequestFilter {

    private final LoggingAutoConfig loggingAutoConfig;

    @Autowired
    public LoggingFilter(LoggingAutoConfig loggingAutoConfig) {
        this.loggingAutoConfig = loggingAutoConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            MDCUtil.addRequestHeaders(request);
            boolean ignore = loggingAutoConfig.getIgnorePatterns().stream()
                    .anyMatch(ignorePattern -> request.getRequestURI().matches(ignorePattern));
            if (ignore) {
                filterChain.doFilter(request, response);
            } else {
                CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
                logRequest(cachedHttpServletRequest);
                final CachedHttpServletResponse cachedHttpServletResponse = new CachedHttpServletResponse(response);
                MDCUtil.addResponseHeaders(request, cachedHttpServletResponse);
                try {
                    filterChain.doFilter(cachedHttpServletRequest, cachedHttpServletResponse);
                    logResponse(cachedHttpServletResponse);
                } catch (Exception e) {
                    logResponse(cachedHttpServletResponse);
                    throw e;
                }
            }
        } finally {
            MDCUtil.clearHeaders();
        }
    }

    private void logRequest(CachedHttpServletRequest cachedHttpServletRequest) throws IOException {
        if (Objects.nonNull(loggingAutoConfig.getRequestConfig()) && loggingAutoConfig.getRequestConfig().isEnabled()) {
            log.info("Request :: application :: {}, method={}, uri={}, payload={}, headers={}", loggingAutoConfig.getApplicationName(), cachedHttpServletRequest.getMethod(),
                    cachedHttpServletRequest.getRequestURI(), IOUtils.toString(cachedHttpServletRequest.getInputStream(),
                            cachedHttpServletRequest.getCharacterEncoding()), cachedHttpServletRequest.getAllHeaders());
        }
    }

    private void logResponse(CachedHttpServletResponse cachedHttpServletResponse) {
        if (Objects.nonNull(loggingAutoConfig.getResponseConfig()) && loggingAutoConfig.getResponseConfig().isEnabled()) {
            log.info("Response :: application :: {}, status={}, payload={}, headers={}", loggingAutoConfig.getApplicationName(), cachedHttpServletResponse.getStatus(),
                    IOUtils.toString(cachedHttpServletResponse.getContent(), cachedHttpServletResponse.getCharacterEncoding()), cachedHttpServletResponse.getAllHeaders());
        }
    }
}

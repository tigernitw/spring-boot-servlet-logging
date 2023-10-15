package com.github.tigernitw.logging.util;

import com.github.tigernitw.logging.servlet.response.CachedHttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MDCUtil {

    private static final String X_REQUEST_ID = "x-request-id";

    public static void addRequestHeaders(HttpServletRequest request) {
        String requestId = request.getHeader(X_REQUEST_ID);
        if (StringUtil.isStringNullOrEmpty(requestId)) {
            requestId = UUID.randomUUID().toString();
        }
        MDC.put(X_REQUEST_ID, requestId);
    }

    public static void addResponseHeaders(HttpServletRequest request, CachedHttpServletResponse cachedHttpServletResponse) {
        String requestId = request.getHeader(X_REQUEST_ID);
        cachedHttpServletResponse.setHeader(X_REQUEST_ID, requestId);
    }

    public static void clearHeaders() {
        MDC.clear();
    }
}

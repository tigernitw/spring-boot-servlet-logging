/*
 * Copyright (c) 2023 Shiva Samadhiya <shiva94.nitw@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.github.tigernitw.logging.filter;

import io.github.tigernitw.logging.config.LoggingAutoConfig;
import io.github.tigernitw.logging.servlet.request.CachedHttpServletRequest;
import io.github.tigernitw.logging.servlet.response.CachedHttpServletResponse;
import io.github.tigernitw.logging.util.MDCUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/** Logging Filter class to log request &amp; response for each servlet. */
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
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      MDCUtil.addRequestHeaders(request);
      boolean ignore =
          loggingAutoConfig.getIgnorePatterns().stream()
              .anyMatch(ignorePattern -> request.getRequestURI().matches(ignorePattern));
      if (ignore) {
        filterChain.doFilter(request, response);
      } else {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
        logRequest(cachedHttpServletRequest);
        final CachedHttpServletResponse cachedHttpServletResponse =
            new CachedHttpServletResponse(response);
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
    if (Objects.nonNull(loggingAutoConfig.getRequestConfig())
        && loggingAutoConfig.getRequestConfig().isEnabled()) {
      log.info(
          "Request :: application :: {}, method={}, uri={}, payload={}, headers={}",
          loggingAutoConfig.getApplicationName(),
          cachedHttpServletRequest.getMethod(),
          cachedHttpServletRequest.getRequestURI(),
          IOUtils.toString(
              cachedHttpServletRequest.getInputStream(),
              cachedHttpServletRequest.getCharacterEncoding()),
          cachedHttpServletRequest.getAllHeaders());
    }
  }

  private void logResponse(CachedHttpServletResponse cachedHttpServletResponse) {
    if (Objects.nonNull(loggingAutoConfig.getResponseConfig())
        && loggingAutoConfig.getResponseConfig().isEnabled()) {
      log.info(
          "Response :: application :: {}, status={}, payload={}, headers={}",
          loggingAutoConfig.getApplicationName(),
          cachedHttpServletResponse.getStatus(),
          IOUtils.toString(
              cachedHttpServletResponse.getContent(),
              cachedHttpServletResponse.getCharacterEncoding()),
          cachedHttpServletResponse.getAllHeaders());
    }
  }
}

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

package io.github.tigernitw.logging.util;

import io.github.tigernitw.logging.servlet.response.CachedHttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

/** {@link MDCUtil} utility class to work with MDC thread local. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MDCUtil {

  private static final String X_REQUEST_ID = "x-request-id";

  /**
   * Method to add unique header for all servlet requests.
   *
   * @param request as argument for fetching header request.
   */
  public static void addRequestHeaders(HttpServletRequest request) {
    String requestId = request.getHeader(X_REQUEST_ID);
    if (StringUtil.isStringNullOrEmpty(requestId)) {
      requestId = UUID.randomUUID().toString();
    }
    MDC.put(X_REQUEST_ID, requestId);
  }

  /**
   * Method to add unique header for all servlet responses.
   *
   * @param request as argument for fetching header in request.
   * @param cachedHttpServletResponse as argument for setting header in response.
   */
  public static void addResponseHeaders(
      HttpServletRequest request, CachedHttpServletResponse cachedHttpServletResponse) {
    String requestId = request.getHeader(X_REQUEST_ID);
    cachedHttpServletResponse.setHeader(X_REQUEST_ID, requestId);
  }

  /** Method to clear all MDC headers. */
  public static void clearHeaders() {
    MDC.clear();
  }
}

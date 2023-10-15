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

package com.github.tigernitw.logging.util;

import com.github.tigernitw.logging.servlet.response.CachedHttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

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

  public static void addResponseHeaders(
      HttpServletRequest request, CachedHttpServletResponse cachedHttpServletResponse) {
    String requestId = request.getHeader(X_REQUEST_ID);
    cachedHttpServletResponse.setHeader(X_REQUEST_ID, requestId);
  }

  public static void clearHeaders() {
    MDC.clear();
  }
}

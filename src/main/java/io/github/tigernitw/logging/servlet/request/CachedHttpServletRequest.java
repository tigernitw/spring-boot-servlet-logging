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

package io.github.tigernitw.logging.servlet.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StreamUtils;

/** Request servlet wrapper to read cached servlet request */
public class CachedHttpServletRequest extends HttpServletRequestWrapper {

  private final byte[] cachedPayload;

  /**
   * Constructs a new {@link CachedHttpServletRequest}.
   *
   * @param request see {@link #request}.
   * @throws IOException If an input or output exception occurred
   */
  public CachedHttpServletRequest(HttpServletRequest request) throws IOException {
    super(request);
    InputStream requestInputStream = request.getInputStream();
    this.cachedPayload = StreamUtils.copyToByteArray(requestInputStream);
  }

  @Override
  public ServletInputStream getInputStream() {
    return new CachedServletInputStream(this.cachedPayload);
  }

  @Override
  public BufferedReader getReader() {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedPayload);
    return new BufferedReader(new InputStreamReader(byteArrayInputStream));
  }

  /**
   * Method to fetch all request servlet headers.
   *
   * @return see {@link Map}
   */
  public Map<String, String> getAllHeaders() {
    final Map<String, String> headers = new HashMap<>();
    Collections.list(getHeaderNames()).forEach(key -> headers.put(key, getHeader(key)));
    return headers;
  }
}

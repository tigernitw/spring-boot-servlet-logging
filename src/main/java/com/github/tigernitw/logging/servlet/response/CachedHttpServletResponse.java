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

package com.github.tigernitw.logging.servlet.response;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CachedHttpServletResponse extends HttpServletResponseWrapper {

  private CachedServletOutputStream cachedServletOutputStream;
  private PrintWriter writer;
  private ServletOutputStream outputStream;

  public CachedHttpServletResponse(HttpServletResponse response) {
    super(response);
  }

  @Override
  public ServletOutputStream getOutputStream() throws IOException {
    if (writer != null) {
      throw new IllegalStateException(
          "CachedHttpServletResponse ::getOutputStream() has already been called on this response.");
    }
    if (outputStream == null) {
      outputStream = getResponse().getOutputStream();
      cachedServletOutputStream = new CachedServletOutputStream(outputStream);
    }
    return cachedServletOutputStream;
  }

  @Override
  public PrintWriter getWriter() throws IOException {
    if (outputStream != null) {
      throw new IllegalStateException(
          "CachedHttpServletResponse ::getWriter() has already been called on this response.");
    }
    if (writer == null) {
      cachedServletOutputStream = new CachedServletOutputStream(getResponse().getOutputStream());
      writer =
          new PrintWriter(
              new OutputStreamWriter(
                  cachedServletOutputStream, getResponse().getCharacterEncoding()),
              true);
    }
    return writer;
  }

  @Override
  public void flushBuffer() throws IOException {
    if (writer != null) {
      writer.flush();
    } else if (outputStream != null) {
      cachedServletOutputStream.flush();
    }
  }

  public byte[] getContent() {
    if (cachedServletOutputStream != null) {
      return cachedServletOutputStream.getCopy();
    } else {
      return new byte[0];
    }
  }

  public Map<String, String> getAllHeaders() {
    final Map<String, String> headers = new HashMap<>();
    getHeaderNames().forEach(key -> headers.put(key, getHeader(key)));
    return headers;
  }
}

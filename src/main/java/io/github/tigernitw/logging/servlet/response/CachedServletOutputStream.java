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

package io.github.tigernitw.logging.servlet.response;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/** OutputStream interface implementation to read servlet response */
public class CachedServletOutputStream extends ServletOutputStream {

  private final OutputStream cachedOutputStream;
  private final ByteArrayOutputStream copyStream;

  /**
   * Constructs a new {@link CachedServletOutputStream}.
   *
   * @param outputStream see {@link OutputStream}.
   */
  public CachedServletOutputStream(OutputStream outputStream) {
    this.cachedOutputStream = outputStream;
    this.copyStream = new ByteArrayOutputStream(1024);
  }

  @Override
  public boolean isReady() {
    return true;
  }

  @Override
  public void setWriteListener(WriteListener writeListener) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void write(int b) throws IOException {
    cachedOutputStream.write(b);
    copyStream.write(b);
  }

  /**
   * Method to convert copied response stream into byte array.
   *
   * @return see {@link Byte[]}
   */
  public byte[] getCopy() {
    return copyStream.toByteArray();
  }
}

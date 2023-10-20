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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/** {@link StringUtil} utility class to work with Strings. */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

  /**
   * Method to check if string is null or empty.
   *
   * @param str string as argument
   * @return true if string is null/empty else false
   */
  public static boolean isStringNullOrEmpty(final String str) {
    return str == null || str.trim().isEmpty();
  }
}

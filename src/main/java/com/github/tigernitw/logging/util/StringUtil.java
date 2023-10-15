package com.github.tigernitw.logging.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtil {

    public static boolean isStringNullOrEmpty(final String str) {
        return str == null || str.trim().isEmpty();
    }

}

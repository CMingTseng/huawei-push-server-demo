package com.netease.study.pushserver;

/**
 * Created by hanpfei0306 on 18-7-6.
 */
public class StringUtils {
    public static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }

        if (str1 == null && str2 != null) {
            return false;
        }

        if (str1 != null && str2 == null) {
            return false;
        }

        return str1.equals(str2);
    }
}

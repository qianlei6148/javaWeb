package io.github.qianlei.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类.
 */
public class StringUtil {
    /**
     * 判断为空.
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断为非空.
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static String[] splitString(String body, String s) {
        return body.split(s);
    }
}

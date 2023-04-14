package com.digital.solution.generalservice.utils;

@SuppressWarnings("all")
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private StringUtils(){}

    public static void trimBase64String(StringBuilder sb, String body) {
        int beginIdx = body.indexOf("base64");
        if (beginIdx != -1) {
            sb.append(body, 0, beginIdx + 17).append("...(trimmed)");
            int endIdx = body.indexOf("\",", beginIdx);
            if (endIdx != -1) {
                sb.append(body.substring(endIdx));
            }
        } else {
            sb.append(body);
        }
    }
}

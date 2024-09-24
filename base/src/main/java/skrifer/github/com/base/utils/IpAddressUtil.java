package skrifer.github.com.base.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP地址工具类
 */
public class IpAddressUtil {

    /**
     * 从text中获取IPV4的地址集合
     * @param text
     * @return
     */
    public static List<String> getIpV4Address(String text) {
        if (text == null) {
            return Collections.emptyList();
        }
        String ipPattern = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
        Pattern pattern = Pattern.compile(ipPattern);
        Matcher matcher = pattern.matcher(text);
        List<String> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(matcher.group());
        }
        return result;
    }
}

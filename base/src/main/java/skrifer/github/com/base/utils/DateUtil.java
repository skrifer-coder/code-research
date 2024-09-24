package skrifer.github.com.base.utils;

public class DateUtil {

    /**
     * 多少秒格式化成HH:mm:ss 显示
     * @param seconds
     * @return
     */
    public static String secondFormatHHmmss(long seconds) {
        long minutes = seconds / 60;
        long hours = minutes / 60;
        return String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60);
    }
}

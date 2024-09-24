package skrifer.github.com.base.utils;

/**
 * 磁盘工具类
 */
public class DiskUtil {

    /**
     * 将字节大小 转换为B MB GB
     * @param size 字节大小(1byte=1B=8b)
     * @return
     */
    public static String getDiskPrintString(long size){
        long rest = 0;
        if(size < 1024){
            return String.valueOf(size) + "B";
        }else{
            size /= 1024;
        }

        if(size < 1024){
            return String.valueOf(size) + "KB";
        }else{
            rest = size % 1024;
            size /= 1024;
        }

        if(size < 1024){
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((rest * 100 / 1024 % 100)) + "MB";
        }else{
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }
}

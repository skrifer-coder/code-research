package skrifer.github.com.code;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class 字符集及编码 {

    /**
     * 二进制的第一位代表符号位。 1代表‘-’,0代表‘+’。
     */

    /**
     * ascii 0-9 英文字母（大小写） 常见标点符号 0-127 共128个码点
     * 128个号码 用 二进制表示 首位为0 00000000 - 01111111 长度为8bit（一个字节/byte）
     */

    /**
     * GBK 中文国标字符集 一个中文字符 编码为两个字节存储，第一个字节的第一位必须为1
     * 包含 ascii 所有编码并与其保持一致 占一个字节
     * 解码时 按一个个字节来解码，首位为0 则是 ascii字符 按一位处理，首位为1 则是中文 按两个字节处理
     */

    /**
     * unicode 字符集
     * utf-32编码 4个字节表示 一个字符 不足前面全部补0，空间浪费巨大
     *
     * utf-8编码 最长4个字节的可变长度编码方式
     * ascii 仍然是一个字节表示
     * 汉字用 三个字节表示
     * 解码时 单字节(ascii)  01111101 发现字节首位为0 读取单字节数据后处理
     *       两字节 110xxxxx 10xxxxxx  发现字节首位为110开头 读取2个字节数据后处理
     *       三字节 1110xxxx 10xxxxxx 10xxxxxx 发现字节首位为1110开头 读取3个字节数据后处理
     *       四字节 11110xxxx 10xxxxxx 10xxxxxx 10xxxxxx 发现字节首位为11110开头 读取4个字节数据后处理
     *
     *       多字节转utf-8步骤： 找到字符对应的数字码 如 我 = 25105 转成二进制 110 001000 010001 (从左边往右以6位分隔)
     *       -> 11100110 10001000 10010001
     *
     */


    public static void main(String[] args) {
        byte[] bytes = "我1".getBytes(StandardCharsets.UTF_8);
        System.out.println(Arrays.toString(bytes));
        System.out.println(new String(bytes, StandardCharsets.UTF_8));
    }


}

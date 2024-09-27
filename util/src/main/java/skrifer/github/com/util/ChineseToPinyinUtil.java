package skrifer.github.com.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChineseToPinyinUtil {

    public static List<String> toPinyin(String chinese) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        List<String> result = new ArrayList<>();
        char[] chars = chinese.toCharArray();
        for (char c : chars) {
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c >= '\u4e00' && c <= '\u9fa5') {
                try {
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    result.add(pinyinArray[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                result.add(String.valueOf(c));
            }
        }
        return result;
    }

    public static String toCamelCase(String part) {
        StringBuilder camelCaseStr = new StringBuilder();
        if (part.length() > 0) {
            camelCaseStr.append(part.substring(0, 1).toUpperCase());
            camelCaseStr.append(part.substring(1).toLowerCase());
        }
        return camelCaseStr.toString();
    }

    public static void main(String[] args) {
        String chinese = "中文转666拼音";
        List<String> pinyins = toPinyin(chinese);
        String pinyin = pinyins.stream().collect(Collectors.joining(""));
        // 输出可能是 "zhongwenzhuangpinyin" 或其他结果，取决于设置的拼音格式

        //字段注释

        System.out.println("字段注释:");
        System.out.print("//" + chinese);

        System.out.println();
        System.out.println();

        //方法注释
        System.out.println("方法注释:");
        System.out.println("/**");
        System.out.println("* " + chinese);
        System.out.println("**/");

        System.out.println();
        System.out.println();

        //全小写
        System.out.println("全小写:");
        System.out.println(pinyin);

        System.out.println();
        System.out.println();
        //全大写
        System.out.println("全大写:");
        System.out.println(pinyin.toUpperCase());

        System.out.println();
        System.out.println();
        //驼峰
        System.out.println("标准驼峰:");
        System.out.println(pinyins.stream().map(ChineseToPinyinUtil::toCamelCase).collect(Collectors.joining("")));

        //变量或方法名驼峰
        System.out.println();
        System.out.println();
        System.out.println("变量或方法名驼峰:");
        System.out.println(pinyins.get(0) + pinyins.stream().skip(1).map(ChineseToPinyinUtil::toCamelCase).collect(Collectors.joining("")));

    }
}

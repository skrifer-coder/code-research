package skrifer.github.com.base.utils;

import java.text.NumberFormat;

public class CurrencyUtil {
    /**
     * 金融类项目的货币处理，一般取2位进度,通常的做法是计算时扩大100，显示时缩小100倍，减少精度误差
     * @param price
     * @return
     */
    public static String getLabelPrice(int price) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);//设置最大小数位
        numberFormat.setMinimumFractionDigits(2);//设置最小小数位 不足自动补0
        return numberFormat.format((price / 100));
    }

    public static void main(String[] args) {
        System.out.println(getLabelPrice(5600));
    }
}

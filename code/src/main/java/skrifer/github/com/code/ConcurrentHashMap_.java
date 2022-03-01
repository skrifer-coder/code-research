package skrifer.github.com.code;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMap_ {
    /**
     * https://blog.csdn.net/xingxiupaioxue/article/details/88062163
     * @param args
     */

    public static void main(String[] args) {
        //jdk7
        //基于segment分段实现，每隔segment内部都有数组+链表（segment内部类似一个小型hashmap），扩容方式也与hashmap类似


        //jdk 8
        //不在基于segment实现
        //扩容方式与hashmap类似，区别在多线程的支持，当ConcurrentHashMap正在扩容时，其他多线程进行put时，会一起加入到扩容工作中来，转移老数据到新数组时，多线程会各自分组分段协作。


        Map<String, Long> usbOnlineDevices1 = new ConcurrentHashMap<>();
        usbOnlineDevices1.put("1", 2L);
        Map<String, Long> usbOnlineDevices2 = new ConcurrentHashMap<>();
        usbOnlineDevices2.put("2", 2L);

        usbOnlineDevices1.keySet().addAll(usbOnlineDevices2.keySet());
    }


}

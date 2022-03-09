package skrifer.github.com.code;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用来代替ArrayList 在并发的不足
 */
public class CopyOnWriteArrayList_ {

    public static void main(String[] args) {


        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();//ReentrantLock 加锁
        list.add("");// 先加锁防止并发 对操作的数组copy复制出来一个新的，写的操作在复制出来的数组上完成(容量+1)， 此时读的操作仍然在原数组上执行,写的操作完成后将引用指针指向新的数组
        //可能会读到非实时数据, 复制数组性能消耗比较大

        //vector也是线程安全的，但是目前已经被CopyOnWriteArrayList取代了
        //vector扩容默认是原始的容量翻倍,比较消耗资源(CopyOnWriteArrayList 扩容只是+1)
        //vector操作都加上synchronized 很影响性能

    }
}

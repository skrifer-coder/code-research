package skrifer.github.com.code;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 用来代替ArrayList 在并发的不足
 */
public class CopyOnWriteArrayList_ {

    public static void main(String[] args) {


        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();//ReentrantLock 加锁
        list.add("");// 先加锁防止并发 对操作的数组copy复制出来一个新的，写的操作在复制出来的数组上完成， 此时读的操作仍然在原数组上执行,写的操作完成后将引用指针指向新的数组
        //可能会读到非实时数据, 复制数组性能消耗比较大


    }
}

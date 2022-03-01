package skrifer.github.com.code;

import java.util.ArrayList;
import java.util.LinkedList;

public class ArrayList_LinkedList {





    public static void main(String[] args) {
        ArrayList<String> arrayList = new ArrayList<>();//底层是数组 查询速度块 方便快捷 实现了List接口

        arrayList.add("111");//有可能会出发数组扩容 影响速度

        arrayList.add(1, "");//定位快 可能扩容 ，并且会移动原有元素位置


        LinkedList<String> linkedList = new LinkedList<>(); //链表(实现了 Deque接口 可当做队列使用) 修改比较快

        linkedList.add("1111"); //链表添加元素 只是修改指针，速度快 不涉及扩容动作

        linkedList.add(1, "");// 定位位置 只能走轮询 影响速度，添加元素速度快


    }
}

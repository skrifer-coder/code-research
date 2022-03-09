package skrifer.github.com.designpattern.singleton;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Father_Son_Extend {

    static class Father {

        void fck(HashMap hashMap) {
            System.out.println("father fck");
        }
    }

    static class Son extends Father {
        void fck(Map map) {
            System.out.println("son fck");
        }
    }

    public static void main(String[] args) {
        Father father = new Father();
        father.fck(new HashMap());

        Son son = new Son();
        son.fck(new HashMap());





    }
}

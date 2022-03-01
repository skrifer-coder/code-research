package skrifer.github.com.designpattern.singleton;

public class S1 {

    private static S1 instance;

    private S1() {

    }

    public static final S1 getInstance() {
        if (instance == null) {
            synchronized (S1.class) {
                if (instance == null) {
                    instance = new S1(); //double check lock 简称DCL
                }
            }
        }
        return instance;
    }
}

package skrifer.github.com.designpattern.singleton;

public class DoubleCheck {

    private static DoubleCheck instance;

    private DoubleCheck() {

    }

    public static final DoubleCheck getInstance() {
        if (instance == null) {
            synchronized (DoubleCheck.class) {
                if (instance == null) {
                    instance = new DoubleCheck(); //double check lock 简称DCL
                }
            }
        }
        return instance;
    }
}

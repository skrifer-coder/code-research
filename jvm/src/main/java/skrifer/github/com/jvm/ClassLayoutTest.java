package skrifer.github.com.jvm;


import org.openjdk.jol.info.ClassLayout;

/**
 * 对象在内存中的布局
 * object header: markword (8),class pointer(4||8)
 * instance data
 * padding
 */
public class ClassLayoutTest {

    public static class T {
        String s = "11111";
        //        double d;
//        long l;
//
        float f;
        boolean b;
        int i;
//
//        short sh;
//        char c;
//

//        byte by;
    }

    public void m() {
        System.out.println(1111);
    }

    public static void main(String[] args) {
        ClassLayoutTest test = new ClassLayoutTest();
        T o = new T();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        new Thread(test::m, "");
    }
}

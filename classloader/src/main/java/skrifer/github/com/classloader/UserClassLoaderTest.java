package skrifer.github.com.classloader;

public class UserClassLoaderTest {

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return super.loadClass(name);
            }
        };


//        Integer integer = classLoader.loadClass("java.lang.Integer").newInstance();
//        System.out.println();
    }

}

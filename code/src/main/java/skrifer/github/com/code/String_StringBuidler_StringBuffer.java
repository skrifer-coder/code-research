package skrifer.github.com.code;

public class String_StringBuidler_StringBuffer {

    public static void main(String[] args) {

        //不可变
        String a = "1"; // new "1" and a 指向 "1"
        a = "2"; // new "2" and a 指向 "2"

        //可变
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("111");

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("2222");

    }
}

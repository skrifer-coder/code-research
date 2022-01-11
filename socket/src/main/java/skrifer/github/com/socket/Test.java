package skrifer.github.com.socket;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {

        try {

            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("192.168.9.200", 8888), 1000);
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

            CountDownLatch countDownLatch = new CountDownLatch(1);
            new Thread() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(5000);
                        countDownLatch.countDown();
                        while (true) {
                            if(inputStream.available() > 0) {
                                System.out.println("received;");
                            } else {
//                                System.out.println(11111);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("exception");
                    }
                }
            }.start();

            long start = System.currentTimeMillis();
            countDownLatch.await(10, TimeUnit.SECONDS);
            System.out.println(System.currentTimeMillis() - start);
            outputStream.writeLong(3895L);
            outputStream.writeLong(0x18);
            outputStream.writeLong(0);
            outputStream.flush();


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }


}

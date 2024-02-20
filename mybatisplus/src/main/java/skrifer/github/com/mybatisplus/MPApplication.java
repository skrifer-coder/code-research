package skrifer.github.com.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("skrifer.github.com.mybatisplus")
public class MPApplication {
    public static void main(String[] args) {
        SpringApplication.run(MPApplication.class, args);
    }
}

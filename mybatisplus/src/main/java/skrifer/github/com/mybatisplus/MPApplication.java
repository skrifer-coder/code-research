package skrifer.github.com.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("skrifer.github.com.mybatisplus.generate.test.mapper")//此注解会将扫描路径下的所有接口（包含自己编写的业务接口）都会生成实现类，导致spring中 的业务类会重复，会干扰业务注入接口的方式,因此需要精确指定mapper的扫描路径
//e.g  Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'skrifer.github.com.mybatisplus.generate.test.service.ITravelAddressService' available: expected single matching bean but found 2: travelAddressServiceImpl,ITravelAddressService
public class MPApplication {
    public static void main(String[] args) {
        SpringApplication.run(MPApplication.class, args);
    }
}

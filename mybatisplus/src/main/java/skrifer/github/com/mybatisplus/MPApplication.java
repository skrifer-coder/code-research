package skrifer.github.com.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("skrifer.github.com.mybatisplus.generate.test.mapper")//此注解会将扫描路径下的所有接口（包含自己编写的业务接口）都会生成实现类，导致spring中 的业务类会重复，会干扰业务注入接口的方式,因此需要精确指定mapper的扫描路径
//e.g  Caused by: org.springframework.beans.factory.NoUniqueBeanDefinitionException: No qualifying bean of type 'skrifer.github.com.mybatisplus.generate.test.service.ITravelAddressService' available: expected single matching bean but found 2: travelAddressServiceImpl,ITravelAddressService
public class MPApplication {
    public static void main(String[] args) {
        SpringApplication.run(MPApplication.class, args);
    }

    /**
     * 在Springboot中，若是要使用mybatis-plus实现查询分页，首先需要配置一个分页配置类即可，配置之后即可实现分页查询。
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));//如果配置多个插件,切记分页最后添加
        //interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); 如果有多数据源可以不配具体类型 否则都建议配上具体的DbType
        return interceptor;
    }
}

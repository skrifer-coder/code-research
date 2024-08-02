package skrifer.github.com.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import skrifer.github.com.es.dto.BizDTO;
import skrifer.github.com.es.service.BizESService;

import javax.annotation.Resource;
import java.util.Collections;

@SpringBootApplication
public class ESSpringApplication {

    @Resource
    private BizESService bizESService;
    public static void main(String[] args) {
//        SpringApplication.main();
        SpringApplication.run(ESSpringApplication.class, args);
    }

    @Bean
    public String add(){
        bizESService.add(Collections.singletonList(new BizDTO("1111", "32222")));
        return "333";
    }
}

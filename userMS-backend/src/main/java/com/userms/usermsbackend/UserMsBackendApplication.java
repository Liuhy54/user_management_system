package com.userms.usermsbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.userms.usermsbackend.mapper")
public class UserMsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserMsBackendApplication.class, args);
    }

}

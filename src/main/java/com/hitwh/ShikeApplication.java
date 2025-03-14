package com.hitwh;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
@Slf4j
@SpringBootApplication
@ServletComponentScan
public class ShikeApplication {
    //测试git
        public static void main(String[] args) {
            SpringApplication.run(ShikeApplication.class, args);
        }

}

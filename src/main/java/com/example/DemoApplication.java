package com.example;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Test
    public void test(){
        Md5Hash md5Hash = new Md5Hash("123123");
        System.out.println(md5Hash);
    }
}

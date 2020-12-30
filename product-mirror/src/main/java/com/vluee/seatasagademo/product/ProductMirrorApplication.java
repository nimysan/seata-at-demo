package com.vluee.seatasagademo.product;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableAutoDataSourceProxy
public class ProductMirrorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductMirrorApplication.class, args);
    }

}

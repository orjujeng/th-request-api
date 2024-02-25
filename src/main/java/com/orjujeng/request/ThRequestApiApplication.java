package com.orjujeng.request;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
//@EnableDiscoveryClient
@EnableRabbit
@MapperScan("com.orjujeng.request.mapper")
public class ThRequestApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ThRequestApiApplication.class, args);
	}
}



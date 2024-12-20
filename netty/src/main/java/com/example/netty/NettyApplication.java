package com.example.netty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories(basePackages = "com.example.netty.domain.repository")
public class NettyApplication {

	public static void main(String[] args) {
		SpringApplication.run(NettyApplication.class, args);
	}

}

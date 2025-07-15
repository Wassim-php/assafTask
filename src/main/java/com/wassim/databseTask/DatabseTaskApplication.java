package com.wassim.databseTask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@EnableAspectJAutoProxy
@SpringBootApplication

public class DatabseTaskApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(DatabseTaskApplication.class, args);
	}

	
}

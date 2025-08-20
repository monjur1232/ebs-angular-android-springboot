package com.erp.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.erp")
@EntityScan(basePackages = {"com.erp"})
@EnableJpaRepositories("com.erp")
public class ERP extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(ERP.class, args);
	}

}

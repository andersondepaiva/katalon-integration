package br.com.andersondepaiva.katalonintegration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "br.com.andersondepaiva")
@EnableEurekaClient
@EnableFeignClients(basePackages = "br.com.andersondepaiva")
public class KatalonIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(KatalonIntegrationApplication.class, args);
	}
}

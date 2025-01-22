package com.healthy.BackEnd;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import java.util.List;


@SpringBootApplication
public class BackEndApplication extends SpringBootServletInitializer {
	@Bean
	public OpenAPI customOpenAPI() {
		Server server = new Server();
		server.setUrl("https://cybriadev.com/api");
		return new OpenAPI().servers(List.of(server));
	}
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BackEndApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}

}

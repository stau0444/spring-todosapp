package com.ugo.todoapp;

import com.ugo.todoapp.commons.web.error.ReadableErrorAttributes;
import com.ugo.todoapp.web.support.Member;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.io.*;
import java.util.Arrays;
import java.util.Base64;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TodosApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodosApplication.class, args);
	}
	@Bean
	public ReadableErrorAttributes errorAttributes(MessageSource messageSource){
		return new ReadableErrorAttributes(messageSource);
	}


}





package com.ugo.todosapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import todoapp.web.WebMvcConfiguration;

@SpringBootApplication
public class TodosApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodosApplication.class, args);
    }
    @Bean
    public WebMvcConfiguration todoappWebMvcConfiguration(){
        return new WebMvcConfiguration();
    }

}

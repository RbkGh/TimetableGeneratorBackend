/*
 * Copyright (c) SwiftPot Solutions Limited
 */

package com.swiftpot.timetable;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class TimetableTechnicalApplication extends CustomServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TimetableTechnicalApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").
                        allowedMethods("POST",
                                "GET",
                                "PUT",
                                "DELETE",
                                "HEAD",
                                "PATCH",
                                "OPTIONS",
                                "TRACE");
            }
        };
    }
}

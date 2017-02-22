package com.swiftpot.timetable;

import com.mongodb.WriteConcern;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.WriteConcernResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class TimetableTechnicalApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TimetableTechnicalApplication.class, args);
    }

    /**
     * allow war file generation by extending {@link SpringBootServletInitializer} and
     * overriding configure,apply plugin 'war' in gradle config too
     * @param application
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TimetableTechnicalApplication.class);
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

    @Bean
    public WriteConcernResolver writeConcernResolver() {
        return action -> {
            System.out.println("**********Using Write Concern of Acknowledged*******");
            return WriteConcern.ACKNOWLEDGED;
        };
    }
}

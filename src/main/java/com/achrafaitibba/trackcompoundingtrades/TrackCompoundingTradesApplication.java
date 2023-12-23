package com.achrafaitibba.trackcompoundingtrades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class TrackCompoundingTradesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackCompoundingTradesApplication.class, args);
    }

}

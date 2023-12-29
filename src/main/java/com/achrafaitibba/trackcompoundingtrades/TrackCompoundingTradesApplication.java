package com.achrafaitibba.trackcompoundingtrades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class TrackCompoundingTradesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackCompoundingTradesApplication.class, args);
    }

    /**
     * // todo database backup if I stop running the server on railway >
     * use my server ? or create a copy in my computer
     * // import/export "excel? or DB? or ...
     */
    // todo data backup should be used to insert it again in case of data loss


}
package com.alivro.spring.sleepyringtail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.alivro.spring.sleepyringtail"})
public class SleepyRingtailApplication {
    public static void main(String[] args) {
        SpringApplication.run(SleepyRingtailApplication.class, args);
    }
}

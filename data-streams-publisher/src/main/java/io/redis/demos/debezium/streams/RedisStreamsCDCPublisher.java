package io.redis.demos.debezium.streams;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@Slf4j
@SpringBootApplication
public class RedisStreamsCDCPublisher {

    public static void main(String[] args) {
        SpringApplication.run(RedisStreamsCDCPublisher.class, args);
    }

}

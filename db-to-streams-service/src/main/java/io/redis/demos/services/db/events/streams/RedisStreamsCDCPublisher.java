package io.redis.demos.services.db.events.streams;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class RedisStreamsCDCPublisher {

    public static void main(String[] args) {
        SpringApplication.run(RedisStreamsCDCPublisher.class, args);
    }

}

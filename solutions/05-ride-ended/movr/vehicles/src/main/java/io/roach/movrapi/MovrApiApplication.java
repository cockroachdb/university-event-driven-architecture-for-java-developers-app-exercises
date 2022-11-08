package io.roach.movrapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableTransactionManagement
public class MovrApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(MovrApiApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MovrApiApplication.class, args);
        logger.info("*** Movr Vehicle API started ***");
    }
}

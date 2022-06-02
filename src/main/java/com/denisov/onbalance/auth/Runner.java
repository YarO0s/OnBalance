package com.denisov.onbalance.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})

@ComponentScan(basePackages = {"com.denisov.onbalance.security"})
@ComponentScan(basePackages = {"com.denisov.onbalance.auth.user"})
@ComponentScan(basePackages = {"com.denisov.onbalance.auth"})
@ComponentScan(basePackages = {"com.denisov.onbalance.email"})
@ComponentScan(basePackages = {"com.denisov.onbalance.auth.confirmation"})
@ComponentScan(basePackages = {"com.denisov.onbalance.activity"})

@EnableJpaRepositories({"com.denisov.onbalance.auth.confirmation",
                        "com.denisov.onbalance.auth.user",
                        "com.denisov.onbalance.activity"})

@EntityScan("com.denisov.onbalance.*")
public class Runner {
    public static void main(String[] args){
        SpringApplication.run(Runner.class, args);
    }
}

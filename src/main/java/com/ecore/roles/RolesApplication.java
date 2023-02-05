package com.ecore.roles;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "The e-core API",
                version = "1.0",
                description = "API to manage roles and memberships for e-core exercise",
                contact = @Contact(
                        name = "Robson Rosa",
                        email = "robsonrosa@outlook.com",
                        url = "http://linkedin.com/in/robsonmrosa")))
public class RolesApplication {

    public static void main(String[] args) {
        SpringApplication.run(RolesApplication.class, args);
    }

}

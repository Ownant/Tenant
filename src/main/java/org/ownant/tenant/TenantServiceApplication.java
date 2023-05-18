package org.ownant.tenant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;

@SpringBootApplication
public class TenantServiceApplication {
    public static void main(String[] args) {
        SpringApplication userServiceApplication = new SpringApplication(TenantServiceApplication.class);
        userServiceApplication.setDefaultProperties(Collections
                .singletonMap("server.port", "8084"));
        userServiceApplication.run(args);
    }
}

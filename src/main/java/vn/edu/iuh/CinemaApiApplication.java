package vn.edu.iuh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CinemaApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApiApplication.class, args);
    }

}

package io.tofts.imagefilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = {"io.tofts.imagefilter.repository"})
public class ImagefilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImagefilterApplication.class, args);

    }
}

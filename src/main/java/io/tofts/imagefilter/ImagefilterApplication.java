package io.tofts.imagefilter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EnableJpaRepositories(basePackages = {"io.tofts.imagefilter.repository"})
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class ImagefilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImagefilterApplication.class, args);

    }
}

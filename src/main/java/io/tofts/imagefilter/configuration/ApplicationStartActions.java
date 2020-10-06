package io.tofts.imagefilter.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class ApplicationStartActions implements ApplicationRunner {

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("Checking if local save folder does exist= "+ applicationConfiguration.getSaveImagesTo());
        Path path = Paths.get(applicationConfiguration.getSaveImagesTo());
        log.info("Folder already exists");
        if (!Files.exists(path)) {
            Files.createDirectory(path);
            log.info("Folder was not present so creating one with path= "+ applicationConfiguration.getSaveImagesTo());
        }

    }
}

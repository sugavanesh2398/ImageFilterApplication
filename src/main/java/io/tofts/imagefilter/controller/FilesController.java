package io.tofts.imagefilter.controller;

import com.drew.imaging.ImageProcessingException;
import io.tofts.imagefilter.services.FilesToDataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    FilesToDataBaseService filesToDataBaseService;

    @PostMapping(value = "/addfiles")
    public ResponseEntity FileProcess(@RequestParam MultipartFile[] files, @RequestParam String userName) throws IOException, ImageProcessingException {

        int count = 0;
        for (MultipartFile file : files) {
            boolean result = filesToDataBaseService.convertFile(file, userName);
            if (result)
                count++;

        }

        if (count == files.length)
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

}

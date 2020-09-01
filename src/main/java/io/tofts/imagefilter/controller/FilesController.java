package io.tofts.imagefilter.controller;

import io.tofts.imagefilter.services.FilesToDataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    FilesToDataBaseService filesToDataBaseService;

    @PostMapping(value = "/addfiles")
    public ResponseEntity FileProcess(@RequestParam String folderPath) throws Exception {

        if (filesToDataBaseService.getFolderContents(folderPath))
            return ResponseEntity.ok().build();

        return ResponseEntity.notFound().build();
    }

}

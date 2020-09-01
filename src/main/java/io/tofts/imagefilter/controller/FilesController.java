package io.tofts.imagefilter.controller;

import io.tofts.imagefilter.services.FilesToDataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.tofts.imagefilter.models.requestmodel.Folderpath;


@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    FilesToDataBaseService filesToDataBaseService;

    @PostMapping(value = "/addfiles")
    public ResponseEntity FileProcess(@RequestBody Folderpath folderpath) throws Exception {

        if (filesToDataBaseService.getFolderContents(folderpath))
            return ResponseEntity.ok().build();

        return ResponseEntity.ok().build();
    }

}

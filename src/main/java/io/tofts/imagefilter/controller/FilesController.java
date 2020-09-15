package io.tofts.imagefilter.controller;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import io.tofts.imagefilter.repository.ImageFilterRepository;
import io.tofts.imagefilter.services.FilesToDataBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    FilesToDataBaseService filesToDataBaseService;
    @Autowired
    ImageFilterRepository repository;
    @PostMapping(value = "/addfiles")
    public ResponseEntity FileProcess(@RequestParam MultipartFile[] files, @RequestParam String userName) throws IOException, ImageProcessingException {

        List<Metadata> metadataList = new ArrayList<Metadata>();
        for (MultipartFile file : files) {
            metadataList.add(filesToDataBaseService.convertFile(file, userName));
        }
        return ResponseEntity.ok(metadataList);

    }
    @PostMapping(value="/flushit/{username}")
    public ResponseEntity DeleteFiles(@RequestParam String userName){
        repository.deleteByUserName(userName);
        return ResponseEntity.ok("files deleted");
    }


}

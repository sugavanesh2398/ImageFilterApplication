package io.tofts.imagefilter.controller;

import com.drew.imaging.FileType;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import io.tofts.imagefilter.models.searchmodel.JpgSearchModel;
import io.tofts.imagefilter.repository.ImageFilterRepository;
import io.tofts.imagefilter.services.FilesToDataBaseService;
import io.tofts.imagefilter.services.JpgSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    JpgSearchService jpgSearchService;

    @PostMapping(value = "/addfiles")
    public ResponseEntity fileProcess(@RequestParam MultipartFile[] files, @RequestParam String userName) throws IOException, ImageProcessingException {

        List<Metadata> metadataList = new ArrayList<Metadata>();
        for (MultipartFile file : files) {
            metadataList.add(filesToDataBaseService.convertAndSaveFile(file, userName));
        }
        return ResponseEntity.ok(metadataList);

    }

    @PostMapping(value = "/search")
    public ResponseEntity<Resource> searchFiles(@RequestParam String imageFormat, @RequestBody JpgSearchModel jpgSearchModel) {

        if (FileType.Jpeg.getName().equals(imageFormat)) {
            List<Jpg> jpgs = jpgSearchService.jpgSearch(jpgSearchModel);
            List<ResponseEntity<Resource>> files = new ArrayList<>();
            Jpg jpg = jpgs.get(0);
            ResponseEntity<Resource> resourceResponseEntity;
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(MediaType.IMAGE_JPEG_VALUE))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + jpg.getFilename() + "\"")
                    .body(new ByteArrayResource(jpg.getImageFile()));
        }

        return null;
    }

    @PostMapping(value = "/flushit/{username}")
    public ResponseEntity deleteFiles(@RequestParam String userName) {
        repository.deleteByUserName(userName);
        return ResponseEntity.ok("files deleted");
    }

}
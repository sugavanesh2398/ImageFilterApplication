package io.tofts.imagefilter.controller;

import com.drew.imaging.FileType;
import com.drew.metadata.Metadata;
import io.tofts.imagefilter.configuration.ApplicationConfiguration;
import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import io.tofts.imagefilter.models.searchmodel.JpgSearchModel;
import io.tofts.imagefilter.repository.ImageFilterRepository;
import io.tofts.imagefilter.services.FilesToDataBaseService;
import io.tofts.imagefilter.services.JpgSearchService;
import io.tofts.imagefilter.utils.ImageFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    FilesToDataBaseService filesToDataBaseService;

    @Autowired
    ImageFilterRepository repository;

    @Autowired
    JpgSearchService jpgSearchService;

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    @Autowired
    ImageFileUtils imageFileUtils;

    @PostMapping(value = "/addfiles")
    public ResponseEntity<List<Metadata>> fileProcess(@RequestParam MultipartFile[] files, @RequestParam String userName) {

        List<Metadata> metadataList = filesToDataBaseService.saveMetaDataToDB(files, userName);
        return ResponseEntity.ok(metadataList);

    }

    @PostMapping(value = "/search")
    public ResponseEntity searchFiles(@RequestParam String imageFormat, @RequestBody JpgSearchModel jpgSearchModel) {

        if (FileType.Jpeg.getName().equals(imageFormat)) {
            List<Jpg> jpgs = jpgSearchService.jpgSearch(jpgSearchModel);
            String compressFolderName = Instant.now().toString().replace(":", "-");
            File responseFolder = new File(compressFolderName);
            if (!responseFolder.exists())
                responseFolder.mkdir();
            jpgs.stream().parallel()
                    .forEach(
                            jpg -> {
                                System.out.println(jpg.getFilename());
                                Path path = Paths.get(applicationConfiguration.getSaveImagesTo() + "/mnnk/" + jpg.getFilename());
                                try {
                                    String extension = jpg.getFilename().substring(jpg.getFilename().lastIndexOf("."));
                                    Files.move(Paths.get(applicationConfiguration.getSaveImagesTo() + "/mnnk/" + jpg.getFilename()), Paths.get(compressFolderName + "/" + jpg.getFilename()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
        }

        return null;
    }

    @PostMapping(value = "/flushit/{username}")
    public ResponseEntity deleteFiles(@RequestParam String userName) {
        repository.deleteByUserName(userName);
        return ResponseEntity.ok("files deleted");
    }

}
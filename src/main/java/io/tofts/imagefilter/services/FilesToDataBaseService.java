package io.tofts.imagefilter.services;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import io.tofts.imagefilter.mappers.TagToDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class FilesToDataBaseService {

    @Autowired
    TagToDTO tagToDTO;

    private final String FOLDER_NAME = "imageFolder";

    public Metadata convertFile(MultipartFile file, String userName) throws IOException, ImageProcessingException {

        File convFile = new File(file.getOriginalFilename());
        Path path = Paths.get(FOLDER_NAME);
        if(!Files.exists(path))
            Files.createDirectory(path);
        FileOutputStream fos = new FileOutputStream(FOLDER_NAME+"/"+convFile);
        fos.write(file.getBytes());
        fos.close();
        File folderPath = new File(FOLDER_NAME+"/"+convFile);
        return tagToDTO.getFileMetaData(folderPath, userName);
    }

}


package io.tofts.imagefilter.services;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import io.tofts.imagefilter.mappers.TagToDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Slf4j
@Service
public class FilesToDataBaseService {

    @Autowired
    TagToDTO tagToDTO;

    public boolean convertFile(MultipartFile file) throws IOException, ImageProcessingException {

        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        tagToDTO.convertJpgTag(ImageMetadataReader.readMetadata(convFile));
        convFile.delete();
        return true;
    }

}


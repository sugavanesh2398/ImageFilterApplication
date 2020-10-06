package io.tofts.imagefilter.services;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import io.tofts.imagefilter.configuration.ApplicationConfiguration;
import io.tofts.imagefilter.mappers.TagToDTO;
import io.tofts.imagefilter.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@Service
public class FilesToDataBaseService {

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    TagToDTO tagToDTO;

    public Metadata convertAndSaveFile(MultipartFile file, String userName) throws IOException, ImageProcessingException {

        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(applicationConfiguration.getSaveImagesTo() + "/" + convFile);
        fos.write(file.getBytes());
        fos.close();
        File folderPath = new File(applicationConfiguration.getSaveImagesTo() + "/" + convFile);
        String md5 = fileUtils.getMD5(folderPath);
        File fileWithMD5 = new File(folderPath.getParent(),md5);
        Files.copy(folderPath.toPath(),fileWithMD5.toPath());
//        return tagToDTO.getFileMetaData(folderPath, userName);

        return null;
    }

}


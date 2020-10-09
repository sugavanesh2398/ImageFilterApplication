package io.tofts.imagefilter.services;

import com.drew.metadata.Metadata;
import io.tofts.imagefilter.configuration.ApplicationConfiguration;
import io.tofts.imagefilter.mappers.TagToDTO;
import io.tofts.imagefilter.utils.ImageFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class FilesToDataBaseService {

    private static final String LIMITER = "/";

    @Autowired
    TagToDTO tagToDTO;

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private ImageFileUtils imageFileUtils;

    public List<Metadata> saveMetaDataToDB(MultipartFile[] imageFiles, String userName) {

        List<Metadata> metadataList = new ArrayList<Metadata>();
        Path outputFolderPath = Paths.get(applicationConfiguration.getResponseFolder());
        Path folderPath = Paths.get(applicationConfiguration.getSaveImagesTo() + LIMITER + userName);
        if (!Files.exists(folderPath)) {
            try {
                log.info("Creating Inputfolderpath...");
                Files.createDirectory(folderPath);
            } catch (IOException e) {
                log.error("Exception at File Creation", e);
            }
        }
        if (!Files.exists(outputFolderPath)) {
            try {
                log.info("Creating outputfolderpath...");
                Files.createDirectory(outputFolderPath);
            } catch (IOException e) {
                log.error("Exception at File Creation", e);
            }
        }
        List<String> fileNames = saveAllFilesToFolder(imageFiles, userName);
        fileNames.stream()
                .parallel()
                .forEach(
                        fileName -> {
                            metadataList.add(tagToDTO.getFileMetaData(new File(applicationConfiguration.getSaveImagesTo() + LIMITER + userName + LIMITER + fileName), userName));
                        }
                );
        return null;
    }

    private List<String> saveAllFilesToFolder(MultipartFile[] imageFiles, String userName) {
        List<String> fileNames = new ArrayList<>();
        String folderName = applicationConfiguration.getSaveImagesTo() + LIMITER + userName;
        Arrays.asList(imageFiles).stream().parallel().forEach(imageFile -> {
            try {
                String imageFileExtension = FilenameUtils.getExtension(imageFile.getOriginalFilename());
                log.info("File Extension" + imageFileExtension);
                String filePathWithOriginalName = folderName + LIMITER + imageFile.getOriginalFilename();
                Path saveTarget = Paths.get(filePathWithOriginalName);
                Files.copy(imageFile.getInputStream(), saveTarget);
                String md5 = renameFile(folderName, imageFileExtension, filePathWithOriginalName);
                fileNames.add(md5 + "." + imageFileExtension);
            } catch (IOException e) {
                log.error("IOException while saving file" + e);
            }
        });
        return fileNames;
    }

    private String renameFile(String folderName, String extension, String filePathWithOriginalName) throws IOException {
        String md5 = imageFileUtils.getMD5(new File(filePathWithOriginalName));
        String renameFile = folderName + LIMITER + md5 + "." + extension;
        File md5File = new File(renameFile);
        FileUtils.moveFile(new File(filePathWithOriginalName), md5File);
        return md5;
    }

}


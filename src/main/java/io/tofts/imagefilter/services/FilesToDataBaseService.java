package io.tofts.imagefilter.services;

import com.drew.metadata.Metadata;
import io.tofts.imagefilter.configuration.ApplicationConfiguration;
import io.tofts.imagefilter.mappers.TagToDTO;
import io.tofts.imagefilter.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.io.File;

@Slf4j
@Service
public class FilesToDataBaseService {

    @Autowired
    private ApplicationConfiguration applicationConfiguration;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    TagToDTO tagToDTO;

    public Metadata saveMetaDataToDB(List<String> fileNames, String userName) {

        fileNames.stream().parallel()
                .forEach(
                        fileName -> {
                           tagToDTO.getFileMetaData(new File(applicationConfiguration.getSaveImagesTo()+"/"+userName+"/"+fileName));
                        }
                );
        return null;
    }
}


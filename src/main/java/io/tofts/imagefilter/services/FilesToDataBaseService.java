package io.tofts.imagefilter.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class FilesToDataBaseService {

    public boolean getFolderContents(String folderPath) {

        File directoryPath = new File(folderPath);
        log.info("Folder Path" + folderPath);
        String[] contents = directoryPath.list();
        if (contents != null) {
            for (String content : contents) {
                if (content.contains(".jpg"))
                    log.info("Files with extension jpg" + content);
            }
            return true;
        }
        return false;
    }

}

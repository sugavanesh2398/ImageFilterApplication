package io.tofts.imagefilter.services;

import io.tofts.imagefilter.models.requestmodel.Folderpath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
public class FilesToDataBaseService {

    public boolean getFolderContents(Folderpath folderpath) {

        File directoryPath = new File(folderpath.getFolder());
        log.info("folder path"+folderpath.getFolder());
        String contents[] = directoryPath.list();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i].contains(".jpg"))
                System.out.println(contents[i]);
        }
        return true;

    }

}

package io.tofts.imagefilter.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Slf4j
@Service
public class JpgSearchService {

//    @Autowired
//    ImageFilterRepository imageFilterRepository;
    public ArrayList<String> populateFilesList(File dir){
        ArrayList<String> filesListInDir=new ArrayList<>();
        File[] files = dir.listFiles();
        for(File file : files){
            if(file.isFile())
                filesListInDir.add(file.getAbsolutePath());
            else
                populateFilesList(file);
        }
        return filesListInDir;
    }


}

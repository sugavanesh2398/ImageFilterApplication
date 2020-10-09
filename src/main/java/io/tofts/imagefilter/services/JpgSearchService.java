package io.tofts.imagefilter.services;

import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import io.tofts.imagefilter.models.searchmodel.JpgSearchModel;
import io.tofts.imagefilter.repository.ImageFilterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class JpgSearchService {

    @Autowired
    ImageFilterRepository imageFilterRepository;
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
    public List<Jpg> jpgSearch(JpgSearchModel jpgSearchModel) {

        log.info(jpgSearchModel.toString());

        Jpg jpg = new Jpg()
                .builder()
                .aperture(jpgSearchModel.getAperture())
                .exposureTime(jpgSearchModel.getExposureTime())
                .imageHeight(jpgSearchModel.getImageHeight())
                .imageWidth(jpgSearchModel.getImageWidth())
                .iso(jpgSearchModel.getIso())
                .focalLength(jpgSearchModel.getFocalLength())
                .make(jpgSearchModel.getMake())
                .model(jpgSearchModel.getMake())
                .shutterSpeed(jpgSearchModel.getShutterSpeed())
                .build();

        log.error(jpg.toString());

        List<Jpg> imageList = imageFilterRepository.findAll(Example.of(jpg));

        return imageList;
    }

}

package io.tofts.imagefilter.mappers;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import io.tofts.imagefilter.repository.ImageFilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class TagToDTO {
    @Autowired
    public ImageFilterRepository repository;

    @Autowired
    public Jpg jpg;

    public void convertJpgTag(File file) throws IOException, ImageProcessingException {



        BufferedImage image= ImageIO.read(file);
        System.out.println("getData---"+image.getType());
        jpg.setImageHeight(String.valueOf(image.getHeight()));
        jpg.setImageWidth(String.valueOf(image.getWidth()));
        System.out.println(jpg.getImageWidth());
        repository.save(jpg);

//        for (Directory directory : jpgMetadata.getDirectories()) {
//            for (Tag tag : directory.getTags()) {
//                System.out.println(tag.toString());
//            }
//        }
    }
}

package io.tofts.imagefilter.mappers;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagToDTO {

    public void convertJpgTag(Metadata jpgMetadata) {

        for (Directory directory : jpgMetadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag.toString());
            }
        }
    }
}

package io.tofts.imagefilter.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfiguration {

    @Value("${folder.location}")
    private String saveImagesTo;

    public String getSaveImagesTo() {
        return saveImagesTo;
    }

    public void setSaveImagesTo(String saveImagesTo) {
        this.saveImagesTo = saveImagesTo;
    }
}

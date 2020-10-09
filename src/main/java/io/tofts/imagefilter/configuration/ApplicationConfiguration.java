package io.tofts.imagefilter.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfiguration {

    @Value("${folder.location}")
    private String saveImagesTo;

    @Value("${folder.compress}")
    private String responseFolder;

    public String getResponseFolder() {

        return responseFolder;
    }

    public void setResponseFolder(String responseFolder) {
        this.responseFolder = responseFolder;
    }

    public String getSaveImagesTo() {
        return saveImagesTo;
    }


    public void setSaveImagesTo(String saveImagesTo) {
        this.saveImagesTo = saveImagesTo;
    }
}

package io.tofts.imagefilter.models.searchmodel;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
@Builder
public class JpgSearchModel {

    private Date imageDate;
    private Time makeTime;
    private String imageHeight;
    private String imageWidth;
    private String make;
    private String model;
    private String exposureTime;
    private String fnumber;
    private String iso;
    private String shutterSpeed;
    private String aperture;
    private String focalLength;

}

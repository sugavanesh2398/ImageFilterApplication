package io.tofts.imagefilter.models.searchmodel;

import com.drew.lang.annotations.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@Data
@Nullable
@Builder
public class JpgSearchModels {
    private Date imageDate;
    private Time makeTime;
    private String filename;
    private int fileSizeMin;
    private int filesizeMax;
    private int imageHeightMin;
    private int imageHeightMax;
    private int imageWidthMin;
    private int imageWidthMax;
    private String make;
    private String model;
    private String exposureTime;
    private int isoMin;
    private int isoMax;
    private String fNUmber;
    private String shutterSpeed;
    private String aperture;
    private int focalLengthMin;
    private int focalLengthMax;
}

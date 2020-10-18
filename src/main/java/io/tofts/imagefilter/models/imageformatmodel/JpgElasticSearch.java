package io.tofts.imagefilter.models.imageformatmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "jpgelasticsearch")
public class JpgElasticSearch {

    @Id
    private String md5;

    private String userName;
    private String filename;
    private Date imageDate;
    private int imageHeight;
    private int imageWidth;
    private String make;
    private String model;
    private String exposureTime;
    private int iso;
    private String fNumber;
    private String shutterSpeed;
    private String aperture;
    private int focalLength;
    private int fileSize;
    private Timestamp timeStampFromImage;
    private String timeStamp;
}

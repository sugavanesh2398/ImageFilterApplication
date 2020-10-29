package io.tofts.imagefilter.models.imageformatmodel;

import com.drew.lang.annotations.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;


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
    private Integer imageHeight;
    private Integer imageWidth;
    private String make;
    private String model;
    private String exposureTime;
    private Integer iso;
    private String fNumber;
    private String shutterSpeed;
    private String aperture;
    private Integer focalLength;
    private Integer fileSize;
    private Date timeStampFromImage;
    private String timeStamp;
}

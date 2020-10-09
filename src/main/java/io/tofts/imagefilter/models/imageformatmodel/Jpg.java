package io.tofts.imagefilter.models.imageformatmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Jpg {

    @Id
    private String md5;
    private String userName;
    private String filename;
    private Date imageDate;
    private String imageHeight;
    private String imageWidth;
    private String make;
    private String model;
    private String exposureTime;
    private String iso;
    private String shutterSpeed;
    private String aperture;
    private String focalLength;
    private String fileSize;
    private Timestamp timeStampFromImage;
    private String timeStamp;

}

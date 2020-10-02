package io.tofts.imagefilter.models.imageformatmodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Jpg {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String imageId;
    private String userName;
    private String filename;
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
    private String md5;
    private String fileSize;
    private Timestamp timeStampFromImage;
    @Lob
    private byte[] imageFile;
    private Timestamp timeStamp;

}

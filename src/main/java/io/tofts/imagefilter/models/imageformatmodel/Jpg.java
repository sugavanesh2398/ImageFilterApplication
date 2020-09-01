package io.tofts.imagefilter.models.imageformatmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Jpg {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String imageId;
    private String userName;
    private Date imageDate;
    private Time imageTime;
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
    private Timestamp timestamp;

}

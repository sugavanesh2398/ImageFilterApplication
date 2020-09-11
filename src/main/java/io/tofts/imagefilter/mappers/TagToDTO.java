package io.tofts.imagefilter.mappers;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import io.tofts.imagefilter.repository.ImageFilterRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;

@Slf4j
@Component
public class TagToDTO {
    @Autowired
    public ImageFilterRepository repository;

    public void convertJpgTag(File file, String userName) throws IOException, ImageProcessingException {

        Jpg jpg = new Jpg();
        Metadata jpgMetadata = ImageMetadataReader.readMetadata(file);
        ExifSubIFDDirectory exif = jpgMetadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        FileSystemDirectory ff = jpgMetadata.getFirstDirectoryOfType(FileSystemDirectory.class);
        IptcDirectory dir = jpgMetadata.getFirstDirectoryOfType(IptcDirectory.class);
//        int year = dir.getDate(IptcDirectory.TAG_DATE_CREATED).getYear();
//        int month = dir.getDate(IptcDirectory.TAG_DATE_CREATED).getMonth();
//        int date = dir.getDate(IptcDirectory.TAG_DATE_CREATED).getDate();
//        System.out.println(exif.getDateOriginal() + " " + exif.getDateModified() + " " + exif.getDateDigitized());
//        System.out.println(exif.getDateOriginal().getMonth() + " " + exif.getDateModified().getMonth() + " " + exif.getDateModified().getMonth());
//        System.out.println(dir.getDateCreated() + " " + dir.getDateCreated().getMonth());
//        check the date and time. we aren't getting propeer results
//        Date datevalue = new Date(date, month, year);

//        jpg.setImageDate(datevalue);

        jpg.setUserName(userName);
        if (exif.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH) != null)
            jpg.setImageWidth(exif.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
        if (exif.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT) != null)
            jpg.setImageHeight(exif.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
        if (exif.getString(ExifSubIFDDirectory.TAG_LENS_MAKE) != null)
            jpg.setMake(exif.getString(ExifSubIFDDirectory.TAG_LENS_MAKE));
        if (exif.getString(ExifSubIFDDirectory.TAG_LENS_MODEL) != null)
            jpg.setModel(exif.getString(ExifSubIFDDirectory.TAG_LENS_MODEL));
        if (exif.getString(ExifSubIFDDirectory.TAG_APERTURE) != null)
            jpg.setAperture(exif.getString(ExifSubIFDDirectory.TAG_APERTURE));
        if (exif.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME) != null)
            jpg.setExposureTime(exif.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
        if (exif.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH) != null)
            jpg.setFocalLength(exif.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
        if (exif.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT) != null)
            jpg.setIso(exif.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
        if (ff.getString(FileSystemDirectory.TAG_FILE_NAME) != null)
            jpg.setFilename(ff.getString(FileSystemDirectory.TAG_FILE_NAME));
        if (exif.getString(ExifSubIFDDirectory.TAG_SHUTTER_SPEED) != null)
            jpg.setShutterSpeed(exif.getString(ExifSubIFDDirectory.TAG_SHUTTER_SPEED));
        jpg.setMd5(checkSumApacheCommons(file));
        //jpg.setTimestamp(Timestamp.valueOf(exif.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)));
        log.error(jpg.toString());


        repository.save(jpg);
        for (Directory directory : jpgMetadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag.toString());
            }
        }
    }

    public static String checkSumApacheCommons(File file) {
        String checksum = null;
        try {
            checksum = DigestUtils.md5Hex(new FileInputStream(file));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return checksum;
    }

}

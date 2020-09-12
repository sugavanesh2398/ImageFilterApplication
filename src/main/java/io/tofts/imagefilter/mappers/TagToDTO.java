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

@Slf4j
@Component
public class TagToDTO {
    @Autowired
    public ImageFilterRepository repository;

    public void getFileMetaData(File file, String userName) {

        log.error(file.getName());
        if (file.getName().contains("jpg"))
            try {
                fileToJpg(file, userName);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

    }

    private Jpg fileToJpg(File file, String userName) throws ImageProcessingException, IOException {
        Jpg jpg = new Jpg();
        Metadata jpgMetadata = ImageMetadataReader.readMetadata(file);
        ExifSubIFDDirectory exifDirectory = jpgMetadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        FileSystemDirectory fileSystemDirectory = jpgMetadata.getFirstDirectoryOfType(FileSystemDirectory.class);
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
        if (exifDirectory != null) {
            jpg.setImageWidth(exifDirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
            jpg.setImageHeight(exifDirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
            jpg.setMake(exifDirectory.getString(ExifSubIFDDirectory.TAG_LENS_MAKE));
            jpg.setModel(exifDirectory.getString(ExifSubIFDDirectory.TAG_LENS_MODEL));
            jpg.setAperture(exifDirectory.getString(ExifSubIFDDirectory.TAG_APERTURE));
            jpg.setExposureTime(exifDirectory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
            jpg.setFocalLength(exifDirectory.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
            jpg.setIso(exifDirectory.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
            jpg.setShutterSpeed(exifDirectory.getString(ExifSubIFDDirectory.TAG_SHUTTER_SPEED));
            //jpg.setTimestamp(Timestamp.valueOf(exif.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)));
        }

        if (fileSystemDirectory != null) {
            jpg.setFilename(fileSystemDirectory.getString(FileSystemDirectory.TAG_FILE_NAME));
            jpg.setFileSize(fileSystemDirectory.getString(FileSystemDirectory.TAG_FILE_SIZE));
        }

        jpg.setMd5(getMD5(file));
        repository.save(jpg);
        for (Directory directory : jpgMetadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.err.println(tag.toString());
            }
        }

        return jpg;
    }

    private String getMD5(File file) {
        String checksum = null;
        try {
            checksum = DigestUtils.md5Hex(new FileInputStream(file));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return checksum;
    }

}

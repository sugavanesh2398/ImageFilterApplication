package io.tofts.imagefilter.mappers;

import com.drew.imaging.FileType;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import io.tofts.imagefilter.repository.ImageFilterRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class TagToDTO {

    @Autowired
    public ImageFilterRepository imageFilterRepository;

    public Metadata getFileMetaData(File file, String userName) {
        try {
            Metadata metaData = ImageMetadataReader.readMetadata(file);
            FileTypeDirectory fileTypeDirectory = metaData.getFirstDirectoryOfType(FileTypeDirectory.class);
            if (fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME).equals(FileType.Jpeg.getName()))
                 fileToJpg(file, userName, metaData);
            else
                log.error(fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME));
            return metaData;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private Jpg fileToJpg(File file, String userName, Metadata jpgMetaData) throws ImageProcessingException, IOException {
        Jpg jpg = new Jpg();
        ExifSubIFDDirectory exifDirectory = jpgMetaData.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        FileSystemDirectory fileSystemDirectory = jpgMetaData.getFirstDirectoryOfType(FileSystemDirectory.class);

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
            jpg.setTimeStampFromImage(getTimeStamp(exifDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)));
            jpg.setImageFile(getFileBytes(file));
            jpg.setTimeStamp(Timestamp.from(Instant.now()));
        }

        if (fileSystemDirectory != null) {
            jpg.setFilename(fileSystemDirectory.getString(FileSystemDirectory.TAG_FILE_NAME));
            jpg.setFileSize(fileSystemDirectory.getString(FileSystemDirectory.TAG_FILE_SIZE));
        }

        jpg.setMd5(getMD5(file));
        imageFilterRepository.save(jpg);

        return jpg;
    }


    //Helper Functions
    private String getMD5(File file) {
        String checksum = null;
        try {
            checksum = DigestUtils.md5Hex(new FileInputStream(file));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return checksum;
    }

    private byte[] getFileBytes(File file) throws IOException {
        byte[] picInBytes = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(picInBytes);
        fileInputStream.close();
        return picInBytes;
    }

    private Timestamp getTimeStamp(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return Timestamp.valueOf(strDate);
    }

}

package io.tofts.imagefilter.mappers;

import com.drew.imaging.FileType;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import io.tofts.imagefilter.models.imageformatmodel.JpgElasticSearch;
import io.tofts.imagefilter.repository.ImageFilterRepository;
import io.tofts.imagefilter.repository.MetadataElasticSearchRepository;
import io.tofts.imagefilter.utils.ImageFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class TagToDTO {

    @Autowired
    private ImageFilterRepository imageFilterRepository;

    @Autowired
    private ImageFileUtils imageFileUtils;

    @Autowired
    private MetadataElasticSearchRepository metadataElasticSearchRepository;



    public Metadata getFileMetaData(File file, String userName) {
        try {
            Metadata metaData = ImageMetadataReader.readMetadata(file);
            FileTypeDirectory fileTypeDirectory = metaData.getFirstDirectoryOfType(FileTypeDirectory.class);
            if (fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME).equals(FileType.Jpeg.getName()))
                fileToJpg(file, metaData, userName);
            else
                log.error(fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME));
            return metaData;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private JpgElasticSearch fileToJpg(File file, Metadata jpgMetaData, String userName) throws MetadataException {
        log.error(jpgMetaData.toString());
        Jpg jpg = new Jpg();
        JpgElasticSearch jpgElasticSearch=new JpgElasticSearch();
        ExifSubIFDDirectory exifSubIFDDirectory = jpgMetaData.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        FileSystemDirectory fileSystemDirectory = jpgMetaData.getFirstDirectoryOfType(FileSystemDirectory.class);
        ExifIFD0Directory exifIFD0Directory = jpgMetaData.getFirstDirectoryOfType(ExifIFD0Directory.class);

        jpg.setUserName(userName);
        if (exifSubIFDDirectory != null) {
            jpg.setImageWidth(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH));
            jpg.setImageHeight(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
            jpg.setAperture(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_APERTURE));
            jpg.setExposureTime(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
            jpg.setFocalLength(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
            jpg.setIso(exifSubIFDDirectory.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
            jpg.setShutterSpeed(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_SHUTTER_SPEED));
            jpg.setTimeStampFromImage(parseTimeStamp(exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)));
            jpg.setFNumber(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_FNUMBER));
            jpg.setTimeStamp(getCurrentTime());
            jpgElasticSearch.setImageWidth(Integer.parseInt(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH)));
            jpgElasticSearch.setImageHeight(Integer.parseInt(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT)));
            jpgElasticSearch.setAperture(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_APERTURE));
            jpgElasticSearch.setExposureTime(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
            jpgElasticSearch.setFocalLength(Integer.parseInt(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH)));
            jpgElasticSearch.setIso(Integer.parseInt(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT)));
            jpgElasticSearch.setShutterSpeed(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_SHUTTER_SPEED));
            jpgElasticSearch.setTimeStampFromImage(parseTimeStamp(exifSubIFDDirectory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)));
            jpgElasticSearch.setFNumber(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_FNUMBER));
            jpgElasticSearch.setTimeStamp(getCurrentTime());
        }

        if (fileSystemDirectory != null) {
            jpg.setFilename(fileSystemDirectory.getString(FileSystemDirectory.TAG_FILE_NAME));
            jpg.setFileSize(fileSystemDirectory.getString(FileSystemDirectory.TAG_FILE_SIZE));
            jpgElasticSearch.setFilename(fileSystemDirectory.getString(FileSystemDirectory.TAG_FILE_NAME));
            jpgElasticSearch.setFileSize(fileSystemDirectory.getInteger(FileSystemDirectory.TAG_FILE_SIZE));
        }

        if (exifIFD0Directory != null) {
            jpg.setMake(exifIFD0Directory.getString(ExifIFD0Directory.TAG_MAKE));
            jpg.setModel(exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL));
            jpgElasticSearch.setMake(exifIFD0Directory.getString(ExifIFD0Directory.TAG_MAKE));
            jpgElasticSearch.setModel(exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL));
        }

        jpg.setMd5(imageFileUtils.getMD5(file));
        jpgElasticSearch.setMd5(imageFileUtils.getMD5(file));
        metadataElasticSearchRepository.save(jpgElasticSearch);
        //imageFilterRepository.save(jpg);

        return jpgElasticSearch;
    }

    private Timestamp parseTimeStamp(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate = dateFormat.format(date);
        return Timestamp.valueOf(strDate);
    }

    private String getCurrentTime() {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return localDateFormat.format(new Date());
    }

}

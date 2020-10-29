package io.tofts.imagefilter.mappers;

import com.drew.imaging.FileType;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.png.PngChunkType;
import com.drew.imaging.tiff.TiffHandler;
import com.drew.imaging.tiff.TiffMetadataReader;
import com.drew.imaging.tiff.TiffReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.*;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import com.drew.metadata.gif.GifHeaderDirectory;
import com.drew.metadata.jpeg.JpegDirectory;
import com.drew.metadata.png.PngDirectory;
import com.drew.metadata.tiff.DirectoryTiffHandler;
import io.tofts.imagefilter.models.imageformatmodel.JpgElasticSearch;
import io.tofts.imagefilter.repository.MetadataElasticSearchRepository;
import io.tofts.imagefilter.utils.ImageFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TagToDTO {


    @Autowired
    private ImageFileUtils imageFileUtils;

    @Autowired
    private MetadataElasticSearchRepository metadataElasticSearchRepository;



    public Metadata getFileMetaData(File file, String userName) throws IOException {

        try {
            Metadata metaData = ImageMetadataReader.readMetadata(file);

            FileTypeDirectory fileTypeDirectory = metaData.getFirstDirectoryOfType(FileTypeDirectory.class);
            log.info(fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME));
            if (fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME).equals(FileType.Jpeg.getName()) || fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME).equals(FileType.Tiff.getName()) || fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME).equals(FileType.Arw.getName()))
                fileToJpg(file, metaData, userName);
            else
                PngReader(metaData,file);
                log.error("-->"+fileTypeDirectory.getString(FileTypeDirectory.TAG_DETECTED_FILE_TYPE_NAME));
            return metaData;
        } catch (Exception e) {
            log.error(e.getMessage()+"<---");
        }
        return null;
    }

    private JpgElasticSearch fileToJpg(File file, Metadata jpgMetaData, String userName) throws MetadataException {
        log.error(jpgMetaData.toString());

        JpgElasticSearch jpgElasticSearch=new JpgElasticSearch();
        try {
            ExifSubIFDDirectory exifSubIFDDirectory = jpgMetaData.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            FileSystemDirectory fileSystemDirectory = jpgMetaData.getFirstDirectoryOfType(FileSystemDirectory.class);
            ExifIFD0Directory exifIFD0Directory = jpgMetaData.getFirstDirectoryOfType(ExifIFD0Directory.class);

            jpgElasticSearch.setUserName(userName);
            if (exifSubIFDDirectory != null) {


                jpgElasticSearch.setAperture(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_MAX_APERTURE));
                log.info("4"+exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_APERTURE));
                jpgElasticSearch.setExposureTime(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
                log.info("1"+exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
                jpgElasticSearch.setFocalLength(exifSubIFDDirectory.getInteger(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
                log.info("2"+exifSubIFDDirectory.getInteger(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
                jpgElasticSearch.setIso(exifSubIFDDirectory.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
                log.info("3"+exifSubIFDDirectory.getInteger(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
                jpgElasticSearch.setShutterSpeed(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_SHUTTER_SPEED));
                log.info("5"+exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_SHUTTER_SPEED));
                jpgElasticSearch.setFNumber(exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_FNUMBER));
                log.info("6"+exifSubIFDDirectory.getString(ExifSubIFDDirectory.TAG_FNUMBER));
                jpgElasticSearch.setTimeStamp(getCurrentTime());
            }

            if (fileSystemDirectory != null) {
                log.info("fileSystemDirectory");
                jpgElasticSearch.setFilename(fileSystemDirectory.getString(FileSystemDirectory.TAG_FILE_NAME));
                jpgElasticSearch.setFileSize(fileSystemDirectory.getInteger(FileSystemDirectory.TAG_FILE_SIZE));
            }

            if (exifIFD0Directory != null) {
                log.info("exifIFD0Directory");
                jpgElasticSearch.setMake(exifIFD0Directory.getString(ExifIFD0Directory.TAG_MAKE));
                jpgElasticSearch.setModel(exifIFD0Directory.getString(ExifIFD0Directory.TAG_MODEL));
                jpgElasticSearch.setImageHeight(exifIFD0Directory.getInteger(ExifIFD0Directory.TAG_IMAGE_HEIGHT));

                jpgElasticSearch.setImageWidth(exifIFD0Directory.getInteger(ExifIFD0Directory.TAG_IMAGE_WIDTH));
                jpgElasticSearch.setTimeStampFromImage(exifIFD0Directory.getDate(ExifIFD0Directory.TAG_DATETIME_ORIGINAL));
            }

            for(Directory directory: jpgMetaData.getDirectories()){
                //jpeg
//                for (Tag tag : directory.getTags()) {
//                    System.out.println(tag+"---->"+directory);
//                }

                if(directory instanceof JpegDirectory) {
                    log.info("instance of JPegDirectory");
                    jpgElasticSearch.setImageWidth(directory.getInteger(JpegDirectory.TAG_IMAGE_WIDTH));
                    jpgElasticSearch.setImageHeight(directory.getInteger(JpegDirectory.TAG_IMAGE_HEIGHT));
                }
            }
            jpgElasticSearch.setMd5(imageFileUtils.getMD5(file));
            metadataElasticSearchRepository.save(jpgElasticSearch);

            return jpgElasticSearch;
        }catch (Exception e){
            log.error(e.getStackTrace()+"<<*****");
        }
        return jpgElasticSearch;
    }

    private Timestamp parseTimeStamp(Date date) {
        log.info("fdddddaaattteeee");
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        log.info(dateFormat.format(date));
//        String strDate = dateFormat.format(date);
//        log.info(dateFormat.toString());
        Timestamp ts=new Timestamp(date.getTime());
        log.info(ts.toString());
        return ts;
    }

    private String getCurrentTime() {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return localDateFormat.format(new Date());
    }
    public void PngReader(Metadata metadata, File file) throws ImageProcessingException {
        Map<String, Object> map = new HashMap<>();
        Date fileModifiedDate = null;
        Date dateTime = null;
        Integer imageWidth = null;
        Integer imageHeight = null;
        log.error(metadata.toString());
        JpgElasticSearch jpgElasticSearch=new JpgElasticSearch();

        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag+"---->"+directory);
            }
            // Get the picture last modified time
            if (directory instanceof ExifIFD0Directory) {
                dateTime = directory.getDate(ExifDirectoryBase.TAG_DATETIME_ORIGINAL);
                jpgElasticSearch.setTimeStampFromImage(dateTime);
            }



            //png
            if (directory instanceof PngDirectory) {
                PngDirectory pngDirectory = (PngDirectory) directory;
                PngChunkType pngChunkType = pngDirectory.getPngChunkType();
                if (pngChunkType.equals(PngChunkType.IHDR)) {
                    imageWidth = directory.getInteger(PngDirectory.TAG_IMAGE_WIDTH);
                    jpgElasticSearch.setImageWidth(imageWidth);
                    imageHeight = directory.getInteger(PngDirectory.TAG_IMAGE_HEIGHT);

                }
            }

            //gif
            if (directory instanceof GifHeaderDirectory) {
                imageWidth = directory.getInteger(GifHeaderDirectory.TAG_IMAGE_WIDTH);
                jpgElasticSearch.setImageWidth(imageWidth);
                imageHeight = directory.getInteger(GifHeaderDirectory.TAG_IMAGE_HEIGHT);
                jpgElasticSearch.setImageHeight(imageHeight);
            }
        }
        jpgElasticSearch.setMd5(imageFileUtils.getMD5(file));
        metadataElasticSearchRepository.save(jpgElasticSearch);

    }

}

package io.tofts.imagefilter.mappers;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileSystemDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import io.tofts.imagefilter.repository.ImageFilterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Component
public class TagToDTO {
    @Autowired
    public ImageFilterRepository repository;

    @Autowired
    public Jpg jpg;

    public void convertJpgTag(File file) throws IOException, ImageProcessingException {

        Metadata jpgMetadata=ImageMetadataReader.readMetadata(file);
        ExifSubIFDDirectory exif
                = jpgMetadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
        FileSystemDirectory ff=jpgMetadata.getFirstDirectoryOfType(FileSystemDirectory.class);
        IptcDirectory dir=jpgMetadata.getFirstDirectoryOfType(IptcDirectory.class);
        int year=dir.getDate(IptcDirectory.TAG_DATE_CREATED).getYear();
        int month=dir.getDate(IptcDirectory.TAG_DATE_CREATED).getMonth();
        int date=dir.getDate(IptcDirectory.TAG_DATE_CREATED).getDate();
        System.out.println(exif.getDateOriginal()+" "+exif.getDateModified()+" "+exif.getDateDigitized());
        System.out.println(exif.getDateOriginal().getMonth()+" "+exif.getDateModified().getMonth()+" "+exif.getDateModified().getMonth());
        System.out.println(dir.getDateCreated()+" "+dir.getDateCreated().getMonth());
        //check the date and time. we aren't getting propeer results
        Date datevalue=new Date(date,month,year);

        jpg.setImageDate(datevalue);
        jpg.setImageWidth(String.valueOf(exif.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_WIDTH)));
        jpg.setImageHeight(exif.getString(ExifSubIFDDirectory.TAG_EXIF_IMAGE_HEIGHT));
        jpg.setMake(exif.getString(ExifSubIFDDirectory.TAG_LENS_MAKE));
        jpg.setModel(exif.getString(ExifSubIFDDirectory.TAG_LENS_MODEL));
        jpg.setAperture(exif.getString(ExifSubIFDDirectory.TAG_APERTURE));
        jpg.setExposureTime(exif.getString(ExifSubIFDDirectory.TAG_EXPOSURE_TIME));
        jpg.setFocalLength(exif.getString(ExifSubIFDDirectory.TAG_FOCAL_LENGTH));
        jpg.setIso(exif.getString(ExifSubIFDDirectory.TAG_ISO_EQUIVALENT));
        jpg.setFilename(ff.getString(FileSystemDirectory.TAG_FILE_NAME));
        jpg.setShutterSpeed(exif.getString(ExifSubIFDDirectory.TAG_SHUTTER_SPEED));
        //jpg.setTimestamp(Timestamp.valueOf(exif.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL)));

        repository.save(jpg);
        for (Directory directory : jpgMetadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.println(tag.toString());
            }
        }
    }
}

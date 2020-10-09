package io.tofts.imagefilter.controller;

import com.drew.imaging.FileType;
import com.drew.metadata.Metadata;
import io.tofts.imagefilter.configuration.ApplicationConfiguration;
import io.tofts.imagefilter.models.imageformatmodel.Jpg;
import io.tofts.imagefilter.models.searchmodel.JpgSearchModel;
import io.tofts.imagefilter.repository.ImageFilterRepository;
import io.tofts.imagefilter.services.FilesToDataBaseService;
import io.tofts.imagefilter.services.JpgSearchService;
import io.tofts.imagefilter.utils.ImageFileUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RestController
@RequestMapping("/files")
public class FilesController {

    @Autowired
    FilesToDataBaseService filesToDataBaseService;

    @Autowired
    ImageFilterRepository repository;

    @Autowired
    JpgSearchService jpgSearchService;

    @Autowired
    ApplicationConfiguration applicationConfiguration;

    @Autowired
    public ServletContext context;

    @Autowired
    ImageFileUtils imageFileUtils;

    @RequestMapping(value = "/")
    @ApiIgnore
    public void redirect(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }

    @PostMapping(value = "/addfiles")
    public ResponseEntity<List<Metadata>> fileProcess(@RequestParam MultipartFile[] files, @RequestParam String userName) {

        List<Metadata> metadataList = filesToDataBaseService.saveMetaDataToDB(files, userName);
        return ResponseEntity.ok(metadataList);

    }

    @PostMapping(value = "/search")
    public ResponseEntity searchFiles(@RequestParam String imageFormat, @RequestBody JpgSearchModel jpgSearchModel) throws IOException {
        Path path2 = Paths.get(applicationConfiguration.getResponseFolder());
        log.info("Folder already exists");
        if (!Files.exists(path2)) {
            Files.createDirectory(path2);
            log.info("Folder was not present so creating one with path= "+ applicationConfiguration.getResponseFolder());
        }
        if (FileType.Jpeg.getName().equals(imageFormat)) {
            List<Jpg> jpgs = jpgSearchService.jpgSearch(jpgSearchModel);
            String compressFolderName = applicationConfiguration.getResponseFolder()+"/"+Instant.now().toString().replace(":", "-");
            File responseFolder = new File(compressFolderName);
            if (!responseFolder.exists())
                responseFolder.mkdir();
            jpgs.stream().parallel()
                    .forEach(
                            jpg -> {
                                System.out.println(jpg.getFilename());
                                Path path = Paths.get(applicationConfiguration.getSaveImagesTo() + "/Sugavanesh/" + jpg.getFilename());
                                try {
                                    String extension = jpg.getFilename().substring(jpg.getFilename().lastIndexOf("."));
                                    Files.copy(Paths.get(applicationConfiguration.getSaveImagesTo() + "/Sugavanesh/" + jpg.getFilename()), Paths.get(compressFolderName + "/" + jpg.getFilename()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
            File dir=new File(compressFolderName);
            String zipfile=compressFolderName+".zip";
            InputStreamResource resource =null;
            HttpHeaders headers = new HttpHeaders();
            String contentType = "application/zip";
            if (!StringUtils.isEmpty(contentType)) {
                headers.setContentType(MediaType.parseMediaType(contentType));
            }
            try {
                ArrayList<String> listfiles=jpgSearchService.populateFilesList(dir);
                //now zip files one by one
                //create ZipOutputStream to write to the zip file
                FileOutputStream fos = new FileOutputStream(zipfile);
                ZipOutputStream zos = new ZipOutputStream(fos);
                for(String filePath : listfiles){
                    System.out.println("Zipping "+filePath);
                    //for ZipEntry we need to keep only relative file path, so we used substring on absolute path
                    ZipEntry ze = new ZipEntry(filePath.substring(dir.getAbsolutePath().length()+1, filePath.length()));
                    zos.putNextEntry(ze);
                    //read the file and write to ZipOutputStream
                    FileInputStream fis = new FileInputStream(filePath);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, len);
                    }
                    zos.closeEntry();
                    fis.close();
                }
                resource = new InputStreamResource(new FileInputStream(new File(zipfile)));
                zos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String mineType = context.getMimeType(new File(zipfile).getName());
            MediaType mediaType = MediaType.parseMediaType(mineType);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + new File(zipfile).getName())
                    .contentType(mediaType)
                    .body(resource);
        }

        return null;
    }

    @PostMapping(value = "/flushit/{username}")
    public ResponseEntity deleteFiles(@RequestParam String userName) throws IOException {
        Path folderPath=Paths.get(applicationConfiguration.getResponseFolder());
        //Files.deleteIfExists(folderPath);
        FileUtils.deleteDirectory(new File(applicationConfiguration.getResponseFolder()));
        return ResponseEntity.ok("files deleted");
    }

}
package io.tofts.imagefilter.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;

@Slf4j
@Service
public class ImageFileUtils {

    public String getMD5(File file) {
        String checksum = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            checksum = DigestUtils.md5Hex(fileInputStream);
            log.info("checksum for " + file.getName() + " = " + checksum);
            fileInputStream.close();
        } catch (Exception e) {
            log.error("Exception with checksum creation ", e);
        }
        return checksum;
    }

}

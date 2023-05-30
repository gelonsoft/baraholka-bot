package baraholkateam.util;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class Converter {
    private static final Logger LOGGER = LoggerFactory.getLogger(Converter.class);

    public static String convertPhotoToBase64String(File photo) {
        try {
            return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(photo));
        } catch (IOException e) {
            LOGGER.error("Cannot convert photo to base64 string", e);
            return null;
        }
    }

    public static File convertBase64StringToPhoto(String photo) {
        try {
            File photoFile = File.createTempFile("photo", "temp");
            Files.write(Path.of(photoFile.getPath()), Base64.getDecoder().decode(photo));
            return photoFile;
        } catch (IOException e) {
            LOGGER.error("Cannot convert base64 string to photo", e);
            return null;
        }
    }
}

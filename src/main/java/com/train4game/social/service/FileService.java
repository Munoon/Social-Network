package com.train4game.social.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {
    @Value("${upload.path}")
    private String uploadPath;

    public void saveAvatar(MultipartFile avatar, int userId) throws IOException {
        if (avatar == null) {
            return;
        }

        String savePath = uploadPath + "/avatars";
        File uploadDir = new File(savePath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

//        String fileId = UUID.randomUUID().toString();
        String resultFileName = "a" + userId;

        convertToJpgAndWrite(avatar.getInputStream(), new File(savePath + "/" + resultFileName + ".jpg"));
    }

    public byte[] getAvatar(int userId) throws IOException {
        File file = new File(uploadPath + "/avatars/a" + userId + ".jpg");
        InputStream inputStream = new FileInputStream(file);
        return StreamUtils.copyToByteArray(inputStream);
    }

    private void convertToJpgAndWrite(InputStream input, File output) throws IOException {
        BufferedImage image = ImageIO.read(input);
        BufferedImage result = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );

        result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        ImageIO.write(result, "jpg", output);
    }
}

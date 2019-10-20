package com.train4game.social.service;

import com.train4game.social.model.User;
import com.train4game.social.model.UserAvatar;
import com.train4game.social.repository.UserAvatarRepository;
import com.train4game.social.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private UserAvatarRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    public void saveAvatar(MultipartFile avatar, int userId) throws IOException {
        if (avatar == null) { // if user didnt set avatar - dont save it
            return;
        }

        String savePath = uploadPath + "/avatars"; // base path to all avatars
        File uploadDir = new File(savePath);
        if (!uploadDir.exists()) { // if pass does not exist - create it
            uploadDir.mkdir();
        }

        User user = userRepository.getOne(userId); // get user reference to set it to new user avatar
        repository.disableAllAvatars(); // disable all avatars. if we dont do that - we will have database error
        UserAvatar save = repository.save(new UserAvatar(null, user)); // save new user avatar to database

        String resultFileName = "a" + save.getId() + ".jpg"; // file name: 'a' + avatar id + '.jpg'. example: 'a104.jpg'
        String resultPath = savePath + "/" + resultFileName; // result path to save it. example: 'E:\files\avatars\a104.jpg'

        convertToJpgAndWrite(avatar.getInputStream(), new File(resultPath)); // it will convert image to JPG and save it on drive
    }

    public byte[] getAvatar(int userId) throws IOException {
        int id = repository.getCurrentAvatar(userId).getId(); // find current user avatar by user id
        File file = new File(uploadPath + "/avatars/a" + id + ".jpg");
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

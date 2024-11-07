package com.demo.utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUploadUtil {
    public static final String UPLOAD_PATH = "upload/";

    static {
        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
    }
    public static String uploadFile(File file) {
        return uploadFileToLocal(file, UPLOAD_PATH);
    }

    public static String uploadFileToLocal(File file, String targetPath) {
        try {
            Path target = new File(targetPath + File.separator + UUID.randomUUID()+"_"+file.getName()).toPath();
            if (!target.getParent().toFile().exists()) {
                target.getParent().toFile().mkdirs();
            }
            Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File uploaded to: " + target);
            return target.toString();
        } catch (IOException e) {
            System.err.println("Error uploading file: " + e.getMessage());
            return "";
        }
    }

    public static ImageIcon loadImage(String path) {
        ImageIcon image = new ImageIcon(path);
        return image;
    }

    public static ImageIcon loadAvatar(String path) {
        ImageIcon image = new ImageIcon(path);
        ImageIcon icon = FileUploadUtil.loadImage(path);
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Resize the image
        return new ImageIcon(newImg);
    }
}
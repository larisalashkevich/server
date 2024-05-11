package com.example.sources.utils.files;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Data
@AllArgsConstructor
public class FilePathResolver {
    private static String folderPath = "D:\\сохранить\\3 курс\\6сем\\курсач\\проект\\server\\src\\main\\resources\\public\\";

    public static String getFolderPath(){
        return folderPath;
    }
    public static MultipartFile getFile(String path) throws IOException {
        String filePath = folderPath + path;
        return new MockMultipartFile("file", Files.readAllBytes(Paths.get(filePath)));
    }

    public static String saveFile(MultipartFile file) throws IOException {
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String filename = UUID.randomUUID().toString() + extension;
        String filePath = folderPath + filename;
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(file.getBytes());
        }
        return filename;
    }

    public static Boolean deleteFile(String path){
        File file = new File(folderPath + path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static Boolean isFileExists(String path){
        return new File(folderPath + path).exists();
    }
}

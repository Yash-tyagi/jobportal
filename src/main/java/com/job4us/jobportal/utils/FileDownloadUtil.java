package com.job4us.jobportal.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloadUtil {
    private Path foundPath;

    public Resource getFileAsResource(String downloadDir, String fileName) throws IOException {
        Path path = Paths.get(downloadDir);
        Files.list(path).forEach(filePath -> {
            System.out.println(filePath);
            System.out.println(filePath.getFileName());
            if(filePath.getFileName().toString().startsWith(fileName)) {
                foundPath = filePath;
            }
        });
        if(foundPath != null) return new UrlResource(foundPath.toUri());
        return null;
    }

}

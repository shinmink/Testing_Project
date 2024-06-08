package com.sparta.wildcard_newsfeed.util;

import com.sparta.wildcard_newsfeed.exception.customexception.FileException;
import com.sparta.wildcard_newsfeed.exception.customexception.FileSizeExceededException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileUtils {

    private final long IMAGE_MAX_SIZE = 10 * 1024L * 1024L; // 10MB
    private final long VIDEO_MAX_SIZE = 200 * 1024L * 1024L; // 200MB

    @Value("${spring.servlet.multipart.location}")
    private String uploadLocation;

    public String getAbsoluteUploadFolder() {
        File file = new File("");
        String currentAbsolutePath = file.getAbsoluteFile() + uploadLocation;
        Path path = Paths.get(currentAbsolutePath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("사진을 업로드할 폴더를 생성할 수 없습니다.", e);
            }
        }
        return currentAbsolutePath;
    }

    public String createUuidFileName(String originalFileName) {
        String extension = extractExtension(originalFileName);
        return UUID.randomUUID() + "." + extension;
    }

    public String extractOriginalName(String originalFileName) {
        return originalFileName.substring(0, originalFileName.indexOf("."));
    }

    public String extractExtension(String originalFileName) {
        int point = originalFileName.lastIndexOf(".");
        return originalFileName.substring(point + 1);
    }

    public void validFile(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            String originalFilename = file.getOriginalFilename();
            long size = file.getSize();

            String extension = extractExtension(originalFilename).toLowerCase();
            if (Arrays.asList(FileExtensionEnum.IMAGE.getExtensions()).contains(extension)) {
                if (size > IMAGE_MAX_SIZE) {
                    throw new FileSizeExceededException(originalFilename, extension, size, IMAGE_MAX_SIZE);
                }
            } else if (Arrays.asList(FileExtensionEnum.VIDEO.getExtensions()).contains(extension)) {
                if (size > VIDEO_MAX_SIZE) {
                    throw new FileSizeExceededException(originalFilename, extension, size, VIDEO_MAX_SIZE);
                }
            } else {
                throw new FileException("지원하지 않는 파일 확장자입니다. "
                        + FileExtensionEnum.joiningAllExtensions() + "의 확장자만 저장할 수 있습니다.");
            }
        }
    }
}
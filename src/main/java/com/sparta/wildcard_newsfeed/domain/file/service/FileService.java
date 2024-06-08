package com.sparta.wildcard_newsfeed.domain.file.service;

import com.sparta.wildcard_newsfeed.exception.customexception.FileException;
import com.sparta.wildcard_newsfeed.util.FileUtils;
import com.sparta.wildcard_newsfeed.util.S3FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileUtils fileUtils;
    private final S3FileUtils s3FileUtils;

    public File saveFileToLocal(MultipartFile multipartFile, String uuidFileName) {
        String localLocation = fileUtils.getAbsoluteUploadFolder();
        String fullFilePath = localLocation + uuidFileName;

        File saveFile = new File(fullFilePath);
        try {
            multipartFile.transferTo(saveFile);
        } catch (IOException e) {
            throw new FileException("local에 파일을 저장할 수 없습니다.", e);
        }
        return saveFile;
    }

    public String uploadFileToS3(MultipartFile multipartFile) {
        String uuidFileName = fileUtils.createUuidFileName(multipartFile.getOriginalFilename());
        File savedFile = saveFileToLocal(multipartFile, uuidFileName);

        try {
            String s3Url = s3FileUtils.uploadFile(uuidFileName, savedFile);
            if (!deleteFileFromLocal(savedFile)) {
                log.info("S3 업로드 후 로컬 파일 삭제 실패");
            }
            return s3Url;
        } catch (Exception e) {
            throw new FileException("S3에 파일 업로드 실패", e);
        }
    }

    public boolean deleteFileFromLocal(File savedFile) {
        if (!savedFile.delete()) {
            log.error("S3에 파일 저장 후 local 파일 삭제 실패");
            return false;
        }
        return true;
    }
}
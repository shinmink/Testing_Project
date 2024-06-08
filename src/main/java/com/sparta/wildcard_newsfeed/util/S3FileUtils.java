package com.sparta.wildcard_newsfeed.util;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sparta.wildcard_newsfeed.config.S3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3FileUtils {

    private final S3Config s3Config;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(String uuidFileName, File saveFile) {
        s3Config.amazonS3Client().putObject(
                new PutObjectRequest(bucket, uuidFileName, saveFile).withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();
    }
}
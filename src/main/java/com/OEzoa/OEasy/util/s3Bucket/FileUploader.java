package com.OEzoa.OEasy.util.s3Bucket;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Service
public class FileUploader {

    private final S3Client s3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    public FileUploader(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadImage(String imageKey, String imageUrl) {
        try {
            String uniqueImageKey = UUID.randomUUID().toString() + "_" + imageKey;

            // URL로부터 InputStream 가져오기
            URL url = new URL(imageUrl);
            try (InputStream inputStream = url.openStream()) {

                // InputStream을 ByteArrayOutputStream으로 읽어서 크기를 측정
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                inputStream.transferTo(baos);
                byte[] bytes = baos.toByteArray();

                // ByteArrayInputStream을 사용하여 S3에 업로드
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(uniqueImageKey)
                        .build();

                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(bytes));

                return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + uniqueImageKey;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }
}
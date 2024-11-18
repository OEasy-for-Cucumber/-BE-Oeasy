package com.OEzoa.OEasy.util.s3Bucket;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

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

    // 삭제 메서드
    public void deleteImage(String imageKey) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(imageKey)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 삭제에 실패했습니다.", e);
        }
    }

    // 키 뽑는용도
    public String extractKeyFromUrl(String imageUrl) {
        try {
            return imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        } catch (Exception e) {
            throw new IllegalArgumentException("유효하지 않은 S3 URL입니다.", e);
        }
    }
}
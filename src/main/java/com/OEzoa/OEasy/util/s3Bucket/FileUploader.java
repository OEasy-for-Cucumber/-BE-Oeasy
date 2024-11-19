package com.OEzoa.OEasy.util.s3Bucket;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    public String uploadFile(MultipartFile file, String imageKey) {
        try {
            // 파일 확장자 추출
            String extension = getFileExtension(file);
            String fullImageKey = imageKey + "." + extension; // 확장자 추가

            // 파일 데이터를 바이트 배열로 변환
            byte[] fileData = file.getBytes();

            // S3 업로드 요청 생성
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fullImageKey)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(fileData));

            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fullImageKey;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("파일 업로드에 실패했습니다.", e);
        }
    }

    private String getFileExtension(MultipartFile file) {
        // Content-Type으로 확장자 추출
        String contentType = file.getContentType();
        if (contentType == null) {
            return "jpg"; // 기본 확장자
        }

        switch (contentType) {
            case "image/png":
                return "png";
            case "image/jpeg":
            case "image/jpg":
                return "jpg";
            default:
                return "jpg"; // 기본 확장자
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
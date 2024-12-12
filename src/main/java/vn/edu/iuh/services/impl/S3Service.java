package vn.edu.iuh.services.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.edu.iuh.models.enums.UploadType;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {
    @Value("${aws.bucket.name}")
    private String bucketName;
    private final AmazonS3 s3Client;

    @Async
    public String getPresignedUrl(String filename, UploadType type, UUID id) {
        String newFilename;
        if (type == UploadType.AVATAR) {
            newFilename = type.getPath() + id + filename.substring(filename.lastIndexOf("."));
        } else {
            newFilename = type.getPath() + UUID.randomUUID() + filename.substring(filename.lastIndexOf("."));
        }
        return generateUrl(newFilename);
    }

    private String generateUrl(String filename) {
        Instant expirationTime = Instant.now().plus(Duration.ofHours(24));
        return s3Client.generatePresignedUrl(bucketName, filename, Date.from(expirationTime), HttpMethod.PUT)
                       .toString();
    }
}

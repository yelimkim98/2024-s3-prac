package naver.kiel0103.s3test;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class TestBucketAwsS3Service implements AwsS3Service {

    @Value("${cloud.aws.s3}")
    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    @Override
    public String uploadFile(String category, MultipartFile multipartFile) {
        boolean result = validateFileExists(multipartFile);

        if (!result) {
            return null;
        }
        // 파일 경로 생성
        String fileName = CommonUtils.buildFileName(category, multipartFile.getOriginalFilename());

        // 파일 업로드 준비
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(
                    new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );
        } catch (IOException e) {
            return null;
        }
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    private boolean validateFileExists(MultipartFile multipartFile) {
        return !Objects.isNull(multipartFile) && !multipartFile.isEmpty();
    }

    @Override
    public byte[] downloadFileV1(String resourcePath) {
        if (!isFileExistsAtUrl(resourcePath)) {
            return null;
        }
        S3Object s3Object = amazonS3Client.getObject(bucketName, resourcePath);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            return null;
        }
    }

    private boolean isFileExistsAtUrl(String resourcePath) {
        return amazonS3Client.doesObjectExist(bucketName, resourcePath);
    }
}

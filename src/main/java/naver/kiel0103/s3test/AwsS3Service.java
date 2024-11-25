package naver.kiel0103.s3test;

import org.springframework.web.multipart.MultipartFile;

public interface AwsS3Service {

    String uploadFile(String category, MultipartFile multipartFile);
}

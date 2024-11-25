package naver.kiel0103.s3test;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final AwsS3Service s3Service;

    @PostMapping("/upload-file")
    public ResponseEntity<String> upload(
        @RequestParam("category") String category,
        @RequestPart(value = "file") MultipartFile multipartFile
    ) {
        String result = s3Service.uploadFile(category, multipartFile);
        return ResponseEntity.ok().body(result);
    }
}

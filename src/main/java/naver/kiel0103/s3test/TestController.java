package naver.kiel0103.s3test;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/download-file")
    public ResponseEntity<ByteArrayResource> download(
            @RequestParam("resourcePath") String resourcePath
    ) {
        byte[] fileBytes = s3Service.downloadFileV1(resourcePath);
        ByteArrayResource resource = new ByteArrayResource(fileBytes);
        HttpHeaders headers = buildHeaders(resourcePath, fileBytes);

        return ResponseEntity.ok().headers(headers).body(resource);
    }

    private HttpHeaders buildHeaders(String resourcePath, byte[] data) {
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentLength(data.length);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(CommonUtils.createContentDisposition(resourcePath));
        return headers;
    }
}

package naver.kiel0103.s3test;

import org.springframework.http.ContentDisposition;

import java.nio.charset.StandardCharsets;

public class CommonUtils {

    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String CATEGORY_PREFIX = "/";
    private static final String TIME_SEPARATOR = "_";

    public static String buildFileName(String category, String originalFileName) {
        // 파일이름에서 마지막 .의 위치
        int FileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);

        // 파일 이름
        String fileName = originalFileName.substring(0, FileExtensionIndex);

        // 확장자
        String extension = originalFileName.substring(FileExtensionIndex);

        String now = String.valueOf(System.currentTimeMillis());

        return category + CATEGORY_PREFIX + fileName + TIME_SEPARATOR + now + FILE_EXTENSION_SEPARATOR + extension;
    }

    /*
     이 코드의 목적
     */
    public static ContentDisposition createContentDisposition(String categoryWithFileName) {
        String fileName = categoryWithFileName.substring(
                categoryWithFileName.lastIndexOf(CATEGORY_PREFIX) + 1);
        return ContentDisposition.builder("attachment")
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
    }
}

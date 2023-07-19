package test.testspring;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import test.testspring.domain.Product;
import test.testspring.domain.ProductImg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class FIleUpload {
    public String extractExtension(String filename) {
        int lastIndex = filename.lastIndexOf(".");
        if (lastIndex >= 0) {
            return filename.substring(lastIndex);
        }
        return "";
    }
    public boolean uploadFiles(List<MultipartFile> multipartFiles, String fileUploadPath) throws IOException {
        if (multipartFiles.isEmpty()) {

        } else {
            for (MultipartFile multipartFile : multipartFiles) {
                // 파일이 비어 있지 않을 때 작업을 시작해야 오류가 나지 않는다
                if (!multipartFile.isEmpty()) {
                    // jpeg, png, gif 파일들만 받아서 처리할 예정
                    String contentType = multipartFile.getContentType();
                    String originalFilename = multipartFile.getOriginalFilename();

                    // 파일 확장자 추출
                    String extension = extractExtension(originalFilename);
                    if (!isImageFile(extension)) {
                        // 이미지 파일이 아닌 경우 스킵
                        continue;
                    }

                    // 파일 저장을 위한 식별자 생성
                    String uuid = UUID.randomUUID().toString();
                    String storeFilename = uuid + extension;

                    // 저장된 파일로 변경하여 이를 보여주기 위함
                    String filePath = fileUploadPath + "/" + storeFilename;
                    File file = new File(filePath);
                    multipartFile.transferTo(file);
                    // ProductImg 생성
                    return true;
                }
            }

        }
        return false;
    }
    public boolean isImageFile(String extension) {
        return extension.equalsIgnoreCase(".jpg")
                || extension.equalsIgnoreCase(".jpeg")
                || extension.equalsIgnoreCase(".png")
                || extension.equalsIgnoreCase(".gif");
    }
}

package project.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.shop.domain.UploadFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static project.shop.domain.UploadFile.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileService {

  @Value("${file.path}")
  private String fileDir;

  // 파일 경로 가져오기
  public String getFullPath(String filename) {
    return fileDir + filename;
  }

  // 서버전용파일명 생성
  private String createServerFileName(String originalFileName) {
    int pos = originalFileName.lastIndexOf(".");
    String ext = originalFileName.substring(pos + 1);
    return UUID.randomUUID() + "." + ext;
  }

  // 폼에서 받은 멀티파트데이터로 서버전용파일명을 생성하고 UploadFile로 변환
  public UploadFile storeFile(MultipartFile multipartFile) throws IOException {

    String originalFilename = multipartFile.getOriginalFilename();
    String serverFileName = createServerFileName(originalFilename);

    UploadFile uploadFile = createUploadFile(originalFilename, serverFileName);
    File file = new File(fileDir);
    if (!(file.exists())) {
      file.mkdirs();
    }
    multipartFile.transferTo(new File(fileDir + serverFileName));
    return uploadFile;
  }

  // UploadFile을 리스트로 만들어서 반환

  public List<UploadFile> createUploadFileList(List<MultipartFile> images) throws IOException {
    ArrayList<UploadFile> result = new ArrayList<>();
    for (MultipartFile image : images) {
      result.add(storeFile(image));
    }
    return result;
  }

}

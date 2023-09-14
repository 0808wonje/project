package project.shop.repository.uploadfile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import project.shop.domain.UploadFile;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class JpaUploadFileRepository {

  private final EntityManager em;

  public void saveUploadFile(UploadFile uploadFile) {
    em.persist(uploadFile);
  }

  public Optional<UploadFile> findUploadFileById(Long uploadFileId) {
    return Optional.ofNullable(em.find(UploadFile.class, uploadFileId));
  }

  public List<UploadFile> findAllUploadFile() {
    String jpql = "select u from UploadFile u";
    return em.createQuery(jpql, UploadFile.class).getResultList();
  }

  public void deleteUploadFile(Long uploadFileId) {
    Optional<UploadFile> uploadFile = findUploadFileById(uploadFileId);
    if (uploadFile.isPresent()) {
      em.remove(uploadFile.get());
    }
  }
}

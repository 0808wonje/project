package project.shop.repository.uploadfile;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shop.domain.UploadFile;

import java.util.List;
import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}

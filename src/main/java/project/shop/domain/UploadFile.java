package project.shop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "originalFileName", "originalFileName"})
public class UploadFile {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "uploadFile_id")
  private Long id;

  private String originalFileName;
  private String serverFileName;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private Item item;

  public void setItem(Item item) {
    this.item = item;
  }

  public static UploadFile createUploadFile(String originalFileName, String serverFileName) {
    return new UploadFile(originalFileName, serverFileName);
  }

  private UploadFile(String originalFileName, String serverFileName) {
    this.originalFileName = originalFileName;
    this.serverFileName = serverFileName;
  }

}

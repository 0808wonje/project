package project.shop.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;
import project.shop.domain.Region;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemForm {

  @NotBlank
  private String itemName;

  @NotNull
  @Range(min = 1000)
  private Integer price;

  @NotNull
  @Range(min = 1)
  private Integer quantity;

  private String description;

  @NotNull
  private Region salesRegion;

  private List<MultipartFile> images = new ArrayList<>();
}

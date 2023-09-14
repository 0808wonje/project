package project.shop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "comment"})
public class Comment extends BaseEntity {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_id")
  private Long id;

  private String comment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  @NotNull
  private Item item;

  public void assignMemberAndItem(Member member, Item item) {
    this.member = member;
    this.item = item;
    member.getComments().add(this);
    item.getComments().add(this);
  }

  public static Comment createComment(String comment, Member member, Item item) {
    return new Comment(comment, member, item);
  }


  private Comment(String comment, Member member, Item item) {
    this.comment = comment;
    this.member = member;
    this.item = item;
  }

}

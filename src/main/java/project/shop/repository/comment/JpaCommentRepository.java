package project.shop.repository.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import project.shop.domain.Comment;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Transactional
public class JpaCommentRepository {

  private final EntityManager em;

  public void saveComment(Comment comment) {
    em.persist(comment);
  }

  public Optional<Comment> findCommentById(Long commentId) {
    return Optional.ofNullable(em.find(Comment.class, commentId));
  }

  public List<Comment> findAllComment() {
    String jpql = "select c from Comment c";
    return em.createQuery(jpql, Comment.class).getResultList();
  }

  public void deleteComment(Long commentId) {
    Optional<Comment> comment = findCommentById(commentId);
    if (comment.isPresent()) {
      em.remove(comment.get());
    }
  }
}

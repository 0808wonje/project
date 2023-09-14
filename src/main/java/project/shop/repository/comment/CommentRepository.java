package project.shop.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import project.shop.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.testspring.domain.HelpComment;

public interface HelpCommentRepository extends JpaRepository<HelpComment, Long> {

}

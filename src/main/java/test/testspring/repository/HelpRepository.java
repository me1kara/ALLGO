package test.testspring.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.Cart;
import test.testspring.domain.HelpBoard;
public interface HelpRepository extends JpaRepository<HelpBoard, Long> {

    Page<HelpBoard> findByTitleContaining(String title, Pageable pageRequest);
    Page<HelpBoard> findByTitleContainingOrContentContaining(String title, String content, Pageable pageRequest);
    Page<HelpBoard> findByMemberId(String customerId, Pageable pageRequest);


}

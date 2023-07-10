package test.testspring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.testspring.domain.Cart;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findAllByMid(String mid);
    @Modifying
    @Query("delete from Cart c where c.cid in :cnoList")
    void deleteAllByIds(@Param("cnoList") List<Long> cnoList);
}

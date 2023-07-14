package test.testspring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.testspring.domain.ProductImg;

import java.util.List;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {


    @Query("select pi from ProductImg pi where pi.product.product_no = :pid")
    List<ProductImg> findAllByPid(@Param("pid") Long pid);
}

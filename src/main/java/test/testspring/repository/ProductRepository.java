package test.testspring.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.testspring.domain.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select b from Product b where b.product_name like %:keyword%")
    Page<Product> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);
    @Query("select b from Product b where b.product_name like %:keyword% or b.product_content like %:keyword%")
    Page<Product> findByTitleOrContentContaining(@Param("keyword") String keyword, Pageable pageable);
    @Query("select b from Product b where b.seller_id like %:keyword%")
    Page<Product> findByWriterContaining(@Param("keyword") String keyword, Pageable pageable);

}

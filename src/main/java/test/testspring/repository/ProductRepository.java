package test.testspring.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.Product;
import test.testspring.domain.ProductCategory;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p join p.productCategory pc where pc.cateCode = :#{#search.categoryId} and (p.product_name like %:#{#search.keyword}%)")
    Page<Product> findByTitleContaining(@Param("search") SearchDTO searchDTO, Pageable pageable);

    @Query("select p from Product p join p.productCategory pc where pc.cateCode = :#{#search.categoryId} and (p.product_name like %:#{#search.keyword}% or p.product_content like %:#{#search.keyword}%)")
    Page<Product> findByTitleOrContentContaining(@Param("search") SearchDTO search, Pageable pageable);

    @Query("select p from Product p join p.productCategory pc where pc.cateCode = :#{#search.categoryId} and p.seller_id like %:#{#search.keyword}%")
    Page<Product> findByWriterContaining(@Param("search") SearchDTO search, Pageable pageable);

    @Query("select p from Product p join p.productCategory pc where pc.cateCode = :categoryId")
    Page<Product> findAllByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    @Query(value = "select p.* from cart c, product p where c.mid =:mid and c.pno=p.product_no", nativeQuery = true)
    List<Product> getCartList(@Param("mid") String mid);
    @Query(value="select * from product_category pc order by cate_code", nativeQuery = true)
    List<ProductCategory> getCategories();
}

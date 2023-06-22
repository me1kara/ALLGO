package test.testspring.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.Favorite;
import test.testspring.domain.Product;
import test.testspring.domain.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p left join p.productCategory pc where (pc.cateCode = :#{#search.categoryId} or pc.cateParent.cateCode =:#{#search.categoryId}) and p.product_name like %:#{#search.keyword}%")
    Page<Product> findByTitleContainingWithCategoryId(@Param("search") SearchDTO searchDTO, Pageable pageable);
    @Query("select p from Product p left join p.productCategory pc where p.product_name like %:#{#search.keyword}%")
    Page<Product> findByTitleContaining(@Param("search") SearchDTO searchDTO, Pageable pageable);

    @Query("select p from Product p left join p.productCategory pc where p.product_name like %:#{#search.keyword}% or p.product_content like %:#{#search.keyword}%")
    Page<Product> findByTitleOrContentContaining(@Param("search") SearchDTO search, Pageable pageable);
    @Query("select p from Product p left join p.productCategory pc where (pc.cateCode = :#{#search.categoryId} or pc.cateParent.cateCode =:#{#search.categoryId}) and (p.product_name like %:#{#search.keyword}% or p.product_content like %:#{#search.keyword}%)")
    Page<Product> findByTitleOrContentContainingWithCategoryId(@Param("search") SearchDTO search, Pageable pageable);

    @Query("select p from Product p left join p.productCategory pc where (pc.cateCode = :#{#search.categoryId} or pc.cateParent.cateCode =:#{#search.categoryId}) and p.seller_id like %:#{#search.keyword}%")
    Page<Product> findByWriterContainingWithCategoryId(@Param("search") SearchDTO search, Pageable pageable);

    @Query("select p from Product p left join p.productCategory pc where p.seller_id like %:#{#search.keyword}%")
    Page<Product> findByWriterContaining(@Param("search") SearchDTO search, Pageable pageable);

    @Query("select p from Product p join p.productCategory pc where pc.cateCode = :categoryId or pc.cateParent.cateCode = : categoryId")
    Page<Product> findAllByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    @Query(value = "select p.* from cart c, product p where c.mid =:mid and c.pno=p.product_no", nativeQuery = true)
    List<Product> getCartList(@Param("mid") String mid);
    @Query(value="select * from product_category pc order by cate_code", nativeQuery = true)
    List<ProductCategory> getCategories();
    @Query("update Product p set p.view = p.view + 1 where p.product_no = :productNo")
    @Modifying
    @Transactional
    void incrementView(@Param("productNo") Long productNo);

    @Query("select f from Favorite f where f.member_id =:member_id and f.product_no = :product_no")
    Optional<Favorite> validFavoriteProductById(@Param("member_id") String member_id,
                                               @Param("product_no") Long productNo);

    @Modifying
    @Transactional
    @Query("update Product p set p.favorite = p.favorite+1 where p.product_no = :product_no")
    void incrementFavoriteProduct(@Param("product_no") Long productNo);

    @Modifying
    @Transactional
    @Query("update Product p set p.favorite = p.favorite-1 where p.product_no = :product_no")
    void decrementFavoriteProduct(@Param("product_no")Long productNo);


//    @Modifying
//    @Transactional
//    @Query(value = "INSERT INTO favorite(member_id, product_no) VALUES(:member_id, :product_no)", nativeQuery = true)
//    void createFavorite(@Param("member_id") String memberId, @Param("product_no") Long productNo);
//
//
//    @Modifying
//    @Transactional
//    @Query("insert into Favorite f values(f.product_no = :#{#far.product_no}, f.member_id = :#{#far.member_id})")
//    void createFavorite(@Param("far") Favorite far);
}

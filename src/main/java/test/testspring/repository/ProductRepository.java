package test.testspring.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.Favorite;
import test.testspring.domain.Product;
import test.testspring.domain.ProductCategory;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {


    //메인화면 소개란에 전시할 목록

    @Query(value = "SELECT p.* " +
            "FROM product p " +
            "LEFT JOIN ( " +
            "SELECT product_no, COUNT(product_no) AS favorite_count " +
            "FROM favorite " +
            "WHERE created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
            "GROUP BY product_no " +
            ") f ON p.product_no = f.product_no " +
            "GROUP BY p.cate_code " +
            "ORDER BY favorite_count DESC " +
            "LIMIT 6", nativeQuery = true)
    List<Product> findTopSixFavoriteProductsByCategory();

    @Query("select p from Product p left join p.productCategory pc where (pc.cateCode = :#{#search.categoryId} or pc.cateParent.cateCode =:#{#search.categoryId}) and p.product_name like %:#{#search.keyword}%")
    Page<Product> findByTitleContainingWithCategoryId(@Param("search") SearchDTO searchDTO, Pageable pageable);
    @Query("select p from Product p left join p.productCategory pc where p.product_name like %:#{#search.keyword}%")
    Page<Product> findByTitleContaining(@Param("search") SearchDTO searchDTO, Pageable pageable);

    @Query("select p from Product p left join p.productCategory pc where p.product_name like %:#{#search.keyword}% or p.product_content like %:#{#search.keyword}%")
    Page<Product> findByTitleOrContentContaining(@Param("search") SearchDTO search, Pageable pageable);
    @Query("select p from Product p left join p.productCategory pc where (pc.cateCode = :#{#search.categoryId} or pc.cateParent.cateCode =:#{#search.categoryId}) and (p.product_name like %:#{#search.keyword}% or p.product_content like %:#{#search.keyword}%)")
    Page<Product> findByTitleOrContentContainingWithCategoryId(@Param("search") SearchDTO search, Pageable pageable);
    @Query("select p from Product p left join p.productCategory pc where p.member.id like %:#{#search.keyword}%")
    Page<Product> findByWriterContaining(@Param("search") SearchDTO search, Pageable pageable);
    @Query("select p from Product p left join p.productCategory pc where (pc.cateCode = :#{#search.categoryId} or pc.cateParent.cateCode =:#{#search.categoryId}) and p.member.id like %:#{#search.keyword}%")
    Page<Product> findByWriterContainingWithCategoryId(@Param("search") SearchDTO search, Pageable pageable);

    @Query("select p from Product p join p.productCategory pc where pc.cateCode = :categoryId or pc.cateParent.cateCode = : categoryId")
    Page<Product> findAllByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query(value = "select * from product order by favorite desc",nativeQuery = true)
    Page<Product> findAllOrderByFavorite(Pageable pageRequest);

    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN p.productCategory pc " +
            "LEFT JOIN Favorite f ON p.product_no = f.product_no " +
            "WHERE (pc.cateCode = :categoryId OR pc.cateParent.cateCode = :categoryId) " +
            "ORDER BY p.favorite DESC")
    Page<Product> findAllByCategoryIdOrderByFavorite(
            @Param("categoryId") Long categoryId,
            Pageable pageRequest);


    @Query(value = "SELECT p.* " +
            "FROM product p " +
            "LEFT JOIN (" +
            "SELECT product_no, COUNT(product_no) as favorite_count " +
            "FROM favorite " +
            "WHERE created_at >= :date " +
            "GROUP BY product_no" +
            ") f ON p.product_no = f.product_no " +
            "ORDER BY f.favorite_count DESC",
            countQuery = "SELECT count(*) " +
                    "FROM product p " +
                    "LEFT JOIN (" +
                    "SELECT product_no, COUNT(product_no) as favorite_count " +
                    "FROM favorite " +
                    "WHERE created_at >= :date " +
                    "GROUP BY product_no" +
                    ") f ON p.product_no = f.product_no",
            nativeQuery = true)
    Page<Product> findAllByCreatedAtIsAfterOrderByFavorite(@Param("date") Date date, Pageable pageRequest);


    @Query(value = "SELECT p.* " +
            "FROM product p " +
            "LEFT JOIN (" +
            "SELECT product_no, COUNT(product_no) as favorite_count " +
            "FROM favorite " +
            "WHERE created_at >= :date " +
            "GROUP BY product_no" +
            ") f ON p.product_no = f.product_no " +
            "LEFT JOIN product_category pc ON p.cate_code = pc.cate_code " +
            "WHERE pc.cate_code = :categoryId OR pc.cate_parent = :categoryId " +
            "ORDER BY f.favorite_count DESC",
            countQuery = "SELECT count(*) " +
                    "FROM product p " +
                    "LEFT JOIN (" +
                    "SELECT product_no, COUNT(product_no) as favorite_count " +
                    "FROM favorite " +
                    "WHERE created_at >= :date " +
                    "GROUP BY product_no" +
                    ") f ON p.product_no = f.product_no " +
                    "LEFT JOIN product_category pc ON p.cate_code = pc.cate_code " +
                    "WHERE pc.cate_code = :categoryId OR pc.cate_parent = :categoryId",
            nativeQuery = true)
    Page<Product> findAllByCategoryIdAndCreatedAtIsAfterOrderByFavorite(
            @Param("categoryId") Long categoryId,
            @Param("date") Date date,
            Pageable pageRequest);


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
    @Query(value = "select p.* from product p where :rate + 0.1 >= p.discount_rate and p.discount_rate > :rate limit 6", nativeQuery = true)
    List<Product> findLimitedByDiscountRate(@Param("rate") double rate);

    @Query("select p from Product p join p.productCategory pc where (pc.cateCode = :categoryId or pc.cateParent.cateCode = :categoryId) and (:rate + 0.1 >= p.discount_rate and p.discount_rate > :rate)")
    Page<Product> findByCategoryIdAndDiscountRate(@Param("categoryId") Long categoryId, Pageable pageable, @Param("rate") double rate);

    @Query("select p from Product p where :rate + 0.1 >= p.discount_rate and p.discount_rate > :rate")
    Page<Product> findAllByDiscountRate(Pageable pageable, @Param("rate") double rate);

    @Query("select p from Product p left join p.productCategory pc where (pc.cateCode = :#{#search.categoryId} or pc.cateParent.cateCode = :#{#search.categoryId}) and p.product_name like %:#{#search.keyword}% and (:rate + 0.1 >= p.discount_rate and p.discount_rate > :rate)")
    Page<Product> findByCategoryIdAndTitleContainingAndDiscountRate(@Param("search") SearchDTO searchDTO, Pageable pageable, @Param("rate") double rate);

    @Query("select p from Product p left join p.productCategory pc where p.product_name like %:#{#search.keyword}% and (:rate + 0.1 >= p.discount_rate and p.discount_rate > :rate)")
    Page<Product> findByTitleContainingAndDiscountRate(@Param("search") SearchDTO searchDTO, Pageable pageable, @Param("rate") double rate);

    @Query("select p from Product p left join p.productCategory pc where p.product_name like %:#{#search.keyword}% or p.product_content like %:#{#search.keyword}% and (:rate + 0.1 >= p.discount_rate and p.discount_rate > :rate)")
    Page<Product> findByTitleOrContentContainingAndDiscountRate(@Param("search") SearchDTO search, Pageable pageable, @Param("rate") double rate);

    @Query("select p from Product p left join p.productCategory pc where (pc.cateCode = :#{#search.categoryId} or pc.cateParent.cateCode = :#{#search.categoryId}) and (p.product_name like %:#{#search.keyword}% or p.product_content like %:#{#search.keyword}%) and (:rate + 0.1 >= p.discount_rate and p.discount_rate > :rate)")
    Page<Product> findByCategoryIdAndTitleOrContentContainingAndDiscountRate(@Param("search") SearchDTO search, Pageable pageable, @Param("rate") double rate);

    @Query("select p from Product p left join p.productCategory pc where p.member.id like %:#{#search.keyword}% and (:rate + 0.1 >= p.discount_rate and p.discount_rate > :rate)")
    Page<Product> findByWriterContainingAndDiscountRate(@Param("search") SearchDTO search, Pageable pageable, @Param("rate") double rate);

    @Query("select p from Product p left join p.productCategory pc where (pc.cateCode = :#{#search.categoryId} or pc.cateParent.cateCode = :#{#search.categoryId}) and p.member.id like %:#{#search.keyword}% and (:rate + 0.1 >= p.discount_rate and p.discount_rate > :rate)")
    Page<Product> findByCategoryIdAndWriterContainingAndDiscountRate(@Param("search") SearchDTO search, Pageable pageable, @Param("rate") double rate);


    @Query("update Product p set p.amount = p.amount-1 where p.product_no = :productNo")
    @Modifying
    int decreaseAmountOneByProductNo(@Param("productNo") Long productNo);


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

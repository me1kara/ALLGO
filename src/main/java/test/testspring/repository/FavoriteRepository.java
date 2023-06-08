package test.testspring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.testspring.domain.Favorite;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("select f from Favorite f where f.member_id=:id and f.product_no = :product_no")
    Optional<Favorite> findByIdAndProductNo(@Param("id") String id, @Param("product_no") Long productNo);
}

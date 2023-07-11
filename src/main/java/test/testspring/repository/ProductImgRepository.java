package test.testspring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.testspring.domain.Favorite;
import test.testspring.domain.ProductImg;

import java.util.Optional;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {

}

package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.testspring.domain.ProductImg;

public interface ProductImgRepository extends JpaRepository<ProductImg, Long> {


}

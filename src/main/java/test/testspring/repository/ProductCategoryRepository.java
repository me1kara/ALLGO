package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.testspring.domain.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {


}

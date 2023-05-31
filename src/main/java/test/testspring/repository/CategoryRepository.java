package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.testspring.domain.Member;
import test.testspring.domain.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<ProductCategory, Long> {

}

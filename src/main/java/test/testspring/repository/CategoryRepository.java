package test.testspring.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import test.testspring.domain.Member;
import test.testspring.domain.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<ProductCategory, Long> {


}

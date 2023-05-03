package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.testspring.domain.Card;
import test.testspring.domain.Product;

public interface CardRepository extends JpaRepository<Card, Long> {


}

package test.testspring.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import test.testspring.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {



}

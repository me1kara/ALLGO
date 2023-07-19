package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.testspring.domain.Order;
import test.testspring.domain.Product;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Product> findAllById(String id);

    @Query(value = "select * from orders o left join product p ON o.product_no =p.product_no left join delivery d on d.order_id = o.id where o.buyer_id =:buyer",nativeQuery = true)
    List<Order> findAllbyId(@Param("buyer") String id);

}

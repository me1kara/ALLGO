package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import test.testspring.domain.Order;
import test.testspring.domain.Product;

import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Product> findAllById(String id);

    @Query(value = "select * from orders o, product p, delivery d where o.product_no = p.product_no and o.id = d.order_id and o.buyer_id =:buyer and now() between o.payment_at and date_add(o.payment_at, interval 14 day)",nativeQuery = true)
    List<Order> findAllbyIdTo2Week(@Param("buyer") String id);

}

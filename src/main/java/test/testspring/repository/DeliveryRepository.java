package test.testspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.testspring.domain.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

}

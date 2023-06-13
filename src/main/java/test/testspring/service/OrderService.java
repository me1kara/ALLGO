package test.testspring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.testspring.domain.Order;
import test.testspring.repository.OrderRepository;
import javax.transaction.Transactional;

@Transactional
@Service
public class OrderService {
        OrderRepository orderRepository;
        @Autowired
        public OrderService(OrderRepository orderRepository){
            this.orderRepository = orderRepository;
        }
        public Order payment(Order order){
            return orderRepository.save(order);
        }


}

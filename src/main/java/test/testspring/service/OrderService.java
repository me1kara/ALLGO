package test.testspring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.testspring.domain.Cart;
import test.testspring.domain.Order;
import test.testspring.repository.CartRepository;
import test.testspring.repository.OrderRepository;
import test.testspring.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
public class OrderService {
        OrderRepository orderRepository;
        ProductRepository productRepository;

        CartRepository cartRepository;
        @Autowired
        public OrderService(OrderRepository orderRepository,ProductRepository productRepository, CartRepository cartRepository){
            this.orderRepository = orderRepository;
            this.productRepository  = productRepository;
            this.cartRepository= cartRepository;
        }

    public List<Cart> findByCids(List<Long> cnoList) {
            return cartRepository.findAllById(cnoList);
    }

    public Order payment(Order order){
            Order result = orderRepository.save(order);
            int result2 = productRepository.decreaseAmountOneByProductNo(order.getProduct().getProduct_no());

            if(order.getId()!=null){
                if(result2>0){
                    return result;
                }
            }
            return new Order();
        }


    public void deleteCart(Long cid) {
        cartRepository.deleteById(cid);
    }

    public boolean paymentAll(List<Long> cnoList) {
        for(Long cid : cnoList){

        }

        return true;
    }
}

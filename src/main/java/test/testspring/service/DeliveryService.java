package test.testspring.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import test.testspring.domain.Delivery;
import test.testspring.repository.DeliveryRepository;

import javax.transaction.Transactional;

@Service
@Transactional
public class DeliveryService {

    DeliveryRepository deliveryRepository;


    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository){
        this.deliveryRepository = deliveryRepository;
    }
    public Delivery findById(Long id){
        return deliveryRepository.findById(id).orElseThrow();
    }
}

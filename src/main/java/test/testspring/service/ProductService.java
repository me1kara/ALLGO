package test.testspring.service;


import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.Product;
import test.testspring.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Page<Product> getAllProduct(Pageable pageRequest, SearchDTO search){
        String type = search.getSearchType();
        Page<Product> allProduct;
        if (type == null) {
            allProduct=productRepository.findAll(pageRequest);


        } else {
            switch (type) {
                case "title":
                    allProduct = productRepository.findByTitleContaining(search.getKeyword(), pageRequest);
                    break;
                case "titleOrContent":
                    allProduct = productRepository.findByTitleOrContentContaining(search.getKeyword(), pageRequest);
                    break;
                case "writer":
                    allProduct = productRepository.findByWriterContaining(search.getKeyword(), pageRequest);
                    break;
                default:
                    allProduct = productRepository.findAll(pageRequest);
                    break;
            }
        }

        return allProduct;
    }

    public Page<Product> getHotProducts(Pageable pageRequest, SearchDTO search) {
        String type = search.getSearchType();
        Page<Product> hotProducts;
        if (type == null) {
            hotProducts=productRepository.findAll(pageRequest);


        } else {
            switch (type) {
                case "title":
                    hotProducts = productRepository.findByTitleContaining(search.getKeyword(), pageRequest);
                    break;
                case "titleOrContent":
                    hotProducts = productRepository.findByTitleOrContentContaining(search.getKeyword(), pageRequest);
                    break;
                case "writer":
                    hotProducts = productRepository.findByWriterContaining(search.getKeyword(), pageRequest);
                    break;
                default:
                    hotProducts = productRepository.findAll(pageRequest);
                    break;
            }
        }
        return hotProducts;
    }
    public Page<Product> getUsedProducts(Pageable pageRequest, SearchDTO search) {
        String type = search.getSearchType();
        Page<Product> usedProducts;
        if (type == null) {
            usedProducts=productRepository.findAll(pageRequest);
        } else {
            switch (type) {
                case "title":
                    usedProducts = productRepository.findByTitleContaining(search.getKeyword(), pageRequest);
                    break;
                case "titleOrContent":
                    usedProducts = productRepository.findByTitleOrContentContaining(search.getKeyword(), pageRequest);
                    break;
                case "writer":
                    usedProducts = productRepository.findByWriterContaining(search.getKeyword(), pageRequest);
                    break;
                default:
                    usedProducts = productRepository.findAll(pageRequest);
                    break;
            }
        }
        return usedProducts;
    }

    public Product getProductContent(Long productNo) {
        Optional<Product> product = productRepository.findById(productNo);
        return product.get();
    }

}

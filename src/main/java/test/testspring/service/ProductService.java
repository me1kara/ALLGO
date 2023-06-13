package test.testspring.service;


import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.*;
import test.testspring.repository.*;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CategoryRepository categoryRepository;
    private final FavoriteRepository favoriteRepository;
    private final CartRepository cartRepository;
    @Autowired
    public ProductService(ProductRepository productRepository, OrderRepository orderRepository, CategoryRepository categoryRepository, FavoriteRepository favoriteRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.categoryRepository = categoryRepository;
        this.favoriteRepository = favoriteRepository;
        this.cartRepository = cartRepository;
    }

    public Page<Product> getAllProduct(Pageable pageRequest, SearchDTO search){
        String type = search.getSearchType();
        Long categoryId = search.getCategoryId();
        Page<Product> allProduct;
        log.info("info log={}",categoryId,type);
        if (type == null) {
            if(categoryId != null){
                allProduct=productRepository.findAllByCategoryId(categoryId, pageRequest);
            }else {
                allProduct=productRepository.findAll(pageRequest);
            }
        } else {
            switch (type) {
                case "title":
                    allProduct = productRepository.findByTitleContaining(search, pageRequest);
                    break;
                case "titleOrContent":
                    log.info("{}",search.getKeyword());
                    allProduct = productRepository.findByTitleOrContentContaining(search, pageRequest);
                    break;
                case "writer":
                    allProduct = productRepository.findByWriterContaining(search, pageRequest);
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
                    hotProducts = productRepository.findByTitleContaining(search, pageRequest);
                    break;
                case "titleOrContent":
                    hotProducts = productRepository.findByTitleOrContentContaining(search, pageRequest);
                    break;
                case "writer":
                    hotProducts = productRepository.findByWriterContaining(search, pageRequest);
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
                    usedProducts = productRepository.findByTitleContaining(search, pageRequest);
                    break;
                case "titleOrContent":
                    usedProducts = productRepository.findByTitleOrContentContaining(search, pageRequest);
                    break;
                case "writer":
                    usedProducts = productRepository.findByWriterContaining(search, pageRequest);
                    break;
                default:
                    usedProducts = productRepository.findAll(pageRequest);
                    break;
            }
        }
        return usedProducts;
    }

    public Product getProductByNo(Long productNo) {
        Optional<Product> product = productRepository.findById(productNo);
        return product.get();
    }

    public void addViewCount(Long productNo) {
        productRepository.incrementView(productNo);
    }

    public List<Product> getCartList(String mid){
        return productRepository.getCartList(mid);
    }

    public List<Order> getOrderList(String id) {
        return orderRepository.findAllbyIdTo2Week(id);
    }

    public List<ProductCategory> getCateCode() {
        return categoryRepository.findAll();
    }

    public String incrementFavoriteProduct(String id, Long productNo) {
        Optional<Favorite> favorite = productRepository.validFavoriteProductById(id,productNo);
        Long validFavorite = favorite.orElse(new Favorite()).getProduct_no();
        log.info("update favorite {}", validFavorite , id,productNo);
        Favorite far = new Favorite(id,productNo);
        if(validFavorite==null){
            //productRepository.createFavorite(far);
            favoriteRepository.save(far);
            productRepository.incrementFavoriteProduct(productNo);
            return "success";
        }else {
            favoriteRepository.delete(far);
            productRepository.decrementFavoriteProduct(productNo);
            return "fail";
        }

    }
    public String keepProduct(String keeperId, Long productNo, int productCount) {
        Cart cart = Cart.builder().mid(keeperId).pno(productNo).pCount(productCount).build();
        cartRepository.save(cart);
        return "success";
    }

    public String checkMyFavorite(Long productNo, String id) {
        Optional<Favorite> fav = favoriteRepository.findByIdAndProductNo(id,productNo);
        return fav.isPresent() ? "♥":"♡";
    }
}

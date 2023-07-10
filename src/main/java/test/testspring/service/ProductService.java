package test.testspring.service;


import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.*;
import test.testspring.repository.*;

import java.util.Calendar;
import java.util.Date;
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
                    if(categoryId!=null){
                        allProduct = productRepository.findByTitleContainingWithCategoryId(search, pageRequest);
                    }else{
                        allProduct = productRepository.findByTitleContaining(search, pageRequest);
                    }
                    break;
                case "titleOrContent":
                    log.info("{}",search.getKeyword());
                    if(categoryId!=null){
                        allProduct = productRepository.findByTitleOrContentContainingWithCategoryId(search, pageRequest);
                    }else {
                        allProduct = productRepository.findByTitleOrContentContaining(search, pageRequest);
                    }
                    break;
                case "writer":
                    if(categoryId!=null){
                        allProduct = productRepository.findByWriterContainingWithCategoryId(search, pageRequest);
                    } else{
                        allProduct = productRepository.findByWriterContaining(search, pageRequest);
                    }
                    break;
                default:
                    allProduct = productRepository.findAll(pageRequest);
                    break;
            }
        }

        return allProduct;
    }


    public Product getProductByNo(Long productNo) {
        Optional<Product> product = productRepository.findById(productNo);
        return product.get();
    }

    public void addViewCount(Long productNo) {
        productRepository.incrementView(productNo);
    }

    public List<Cart> getCartList(String mid){
        return cartRepository.findAllByMid(mid);
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
        Favorite far = new Favorite(id,productNo,new Date());
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
        Product p = new Product();
        p.setProduct_no(productNo);
        Cart cart = Cart.builder().mid(keeperId).product(p).pCount(productCount).build();
        cartRepository.save(cart);
        return "success";
    }

    public String checkMyFavorite(Long productNo, String id) {
        Optional<Favorite> fav = favoriteRepository.findByIdAndProductNo(id,productNo);
        return fav.isPresent() ? "♥":"♡";
    }


    public Page<Product> getProductRanking(Pageable pageRequest, Long categoryId,String date) {

        Page<Product> productRanking;
        Date targetDate;
        System.out.println(date);
        if (date!=null && (date.equals("daily") || date.equals("weekly") || date.equals("monthly"))) {
            Calendar calendar = Calendar.getInstance();
            switch (date) {
                case "daily":
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                    break;
                case "weekly":
                    calendar.add(Calendar.WEEK_OF_YEAR, -1);
                    break;
                case "monthly":
                    calendar.add(Calendar.MONTH, -1);
                    break;
            }
            targetDate = calendar.getTime();
            if (categoryId != null) {
                productRanking = productRepository.findAllByCategoryIdAndCreatedAtIsAfterOrderByFavorite(categoryId, targetDate, pageRequest);
            } else {
                productRanking = productRepository.findAllByCreatedAtIsAfterOrderByFavorite(targetDate, pageRequest);

            }
        } else {
            if (categoryId != null) {
                productRanking = productRepository.findAllByCategoryIdOrderByFavorite(categoryId, pageRequest);
            } else {
                productRanking = productRepository.findAllOrderByFavorite(pageRequest);
            }
        }

        return productRanking;

    }

    public List<Product> getMainProduct() {
        return productRepository.findTopSixFavoriteProductsByCategory();
    }

    public List<Product> getHotProduct(double rate) {

        return productRepository.findLimitedByDiscountRate(rate);
    }

    public Page<Product> getHotProducts(Pageable pageRequest, SearchDTO search, double rate){
        String type = search.getSearchType();
        Long categoryId = search.getCategoryId();
        Page<Product> allProduct;
        if (type == null) {
            if(categoryId != null){
                allProduct=productRepository.findByCategoryIdAndDiscountRate(categoryId, pageRequest, rate);
            }else {
                allProduct=productRepository.findAllByDiscountRate(pageRequest, rate);
            }
        } else {
            switch (type) {
                case "title":
                    if(categoryId!=null){
                        allProduct = productRepository.findByCategoryIdAndTitleContainingAndDiscountRate(search, pageRequest, rate);
                    }else{
                        allProduct = productRepository.findByTitleContainingAndDiscountRate(search, pageRequest, rate);
                    }
                    break;
                case "titleOrContent":
                    if(categoryId!=null){
                        allProduct = productRepository.findByCategoryIdAndTitleOrContentContainingAndDiscountRate(search, pageRequest, rate);
                    }else {
                        allProduct = productRepository.findByTitleOrContentContainingAndDiscountRate(search, pageRequest, rate);
                    }
                    break;
                case "writer":
                    if(categoryId!=null){
                        allProduct = productRepository.findByCategoryIdAndWriterContainingAndDiscountRate(search, pageRequest, rate);
                    } else{
                        allProduct = productRepository.findByWriterContainingAndDiscountRate(search, pageRequest, rate);
                    }
                    break;
                default:
                    allProduct = productRepository.findAllByDiscountRate(pageRequest, rate);
                    break;
            }
        }
        return allProduct;
    }


    public void cancelCart(List<Long> cnoList) {
        cartRepository.deleteAllByIds(cnoList);
    }
}


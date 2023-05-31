package test.testspring.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import test.testspring.DTO.CategoryDto;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.Product;
import test.testspring.domain.ProductCategory;
import test.testspring.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping("/hotDeal")
    public String showHotdealProdcuts(@RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                      @RequestParam(value = "size",defaultValue = "10",required = false) int size,
                                      SearchDTO search,
                                      Model model){
        Pageable pageRequest = PageRequest.of(page,size);
        Page<Product> pages = productService.getHotProducts(pageRequest, search);
        model.addAttribute("products",pages);

        return "product/hotDeal";
    }

    @RequestMapping("/shopping")
    public String showAllProducts(@RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                                         @RequestParam(value = "size",defaultValue = "10",required = false) int size,
                                                         SearchDTO search,
                                                         Model model) throws JsonProcessingException {
        Pageable pageRequest = PageRequest.of(page,size);
        Page<Product> pages = productService.getAllProduct(pageRequest, search);
        model.addAttribute("products",pages);

        log.info("info text={}",pages.isEmpty());


        List<CategoryDto> cateList = productService.getCateCode().stream().map(CategoryDto::of).collect(Collectors.toList());
        // List<ProductCategory>를 JSON 형식으로 변환, sout 확인용
        //String cateJson = objectMapper.writeValueAsString(cateList);
        model.addAttribute("cateJson", cateList);

        return "product/allProducts";
    }

    @RequestMapping("/used")
    public String showUsedProducts(@RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                   @RequestParam(value = "size",defaultValue = "10",required = false) int size,
                                   SearchDTO search,
                                   Model model){
        Pageable pageRequest = PageRequest.of(page,size);
        Page<Product> pages = productService.getUsedProducts(pageRequest, search);
        model.addAttribute("products",pages);
        return "product/usedProduct";
    }
    @RequestMapping("/ranking")
    public String showProductRanking(){

        return "product/ranking";
    }

    @RequestMapping("/event")
    public String showEventProducts(@RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                    @RequestParam(value = "size",defaultValue = "10",required = false) int size,
                                    SearchDTO search,
                                    Model model){
        Pageable pageRequest = PageRequest.of(page,size);
        Page<Product> pages = productService.getAllProduct(pageRequest, search);
        model.addAttribute("products",pages);

        return "itemList/ranking";
    }

    @GetMapping("/productContent")
    public String showProductContent(@RequestParam(value="product_no") Long product_no, Model model){
        Product product = productService.getProductByNo(product_no);
        model.addAttribute("product",product);
        return "product/productContent";
    }

    @GetMapping("/buyProduct")
    public String processBuyProdcuct(@RequestParam(value="fproduct_no") Long fproduct_no,
                                     @RequestParam(value="select-productCount") int amount,
                                     Model model
                                     ) {
        Product product = productService.getProductByNo(fproduct_no);
        product.setAmount(amount);
        model.addAttribute("product",product);
        model.addAttribute("delivery_price",3000);

        return "product/buyProduct";
    }


}

package test.testspring.controller;


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
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.Product;
import test.testspring.service.ProductService;

import java.util.List;


@Controller
public class ProductController {
    @Autowired
    ProductService productService;

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
                                                         Model model){
        Pageable pageRequest = PageRequest.of(page,size);
        Page<Product> pages = productService.getAllProduct(pageRequest, search);
        model.addAttribute("products",pages);

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

    @GetMapping("productContent")
    public String showProductContent(@RequestParam(value="product_no") Long product_no, Model model){
        Product product = productService.getProductContent(product_no);
        model.addAttribute("product",product);
        return "product/productContent";
    }



}

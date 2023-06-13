package test.testspring.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.testspring.DTO.CategoryDto;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.Card;
import test.testspring.domain.Member;
import test.testspring.domain.Product;
import test.testspring.service.MemberService;
import test.testspring.service.ProductService;

import java.security.Security;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    MemberService memberService;

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        productService.addViewCount(product_no);
        String heart = productService.checkMyFavorite(product_no, id);
        model.addAttribute("product",product);
        model.addAttribute("heart",heart);

        return "product/productContent";
    }

    @GetMapping("/buyProduct")
    public String processBuyProdcuct(@RequestParam(value="fproduct_no") Long fproduct_no,
                                     @RequestParam(value="select-productCount") int amount,
                                     Model model
                                     ) {
        Product product = productService.getProductByNo(fproduct_no);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();

        Member member = memberService.findById(id);
        List<Card> cards = member.getCards();
        product.setAmount(amount);
        model.addAttribute("product",product);
        model.addAttribute("delivery_price",3000);
        model.addAttribute("cards",cards);

        return "product/buyProductForm";
    }

    @RequestMapping("/favorite")
    @ResponseBody
    public String inclementFavorite(@RequestParam("product_no") Long product_no){
        // 현재 로그인된 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String id = authentication.getName();
        String response = productService.incrementFavoriteProduct(id,product_no);
        return response;
    }


    @PostMapping("/keepProduct")
    @ResponseBody
    public String keepProduct(@RequestParam("product_no") Long product_no,
                              @RequestParam("product_count") int product_count){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String keeperId = authentication.getName();
        log.info("save product {}", "");

        return productService.keepProduct(keeperId,product_no,product_count);
    }


}

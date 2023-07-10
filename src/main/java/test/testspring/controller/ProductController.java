package test.testspring.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    public String showHotdealProdcuts(
                                      Model model) throws JsonProcessingException {

        List<Product> twentyProducts = productService.getHotProduct(0.2d);
        List<Product> teenProducts = productService.getHotProduct(0.1d);
        List<Product> fiveProducts = productService.getHotProduct(0);

        model.addAttribute("twentyProducts",twentyProducts);
        model.addAttribute("teenProducts",teenProducts);
        model.addAttribute("fiveProducts",fiveProducts);

        return "product/hotDeal";
    }
    @RequestMapping("/hotDealList")
    public String showHotdealProdcutList(@RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                         @RequestParam(value = "size",defaultValue = "10",required = false) int size,
                                         SearchDTO search,
                                         @RequestParam(value= "discount", required = false) Double discount,
                                         Model model) throws JsonProcessingException {
        Pageable pageRequest = PageRequest.of(page,size);
        Page<Product> pages = productService.getHotProducts(pageRequest, search, discount);
        model.addAttribute("products",pages);
        List<CategoryDto> cateList = productService.getCateCode().stream().map(CategoryDto::of).collect(Collectors.toList());
        // List<ProductCategory>를 JSON 형식으로 변환, sout 확인용
        //String cateJson = objectMapper.writeValueAsString(cateList);
        model.addAttribute("cateJson", cateList);
        model.addAttribute("discount",discount);

        return "product/hotDealList";
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
    @RequestMapping("/ranking")
    public String showProductRanking(@RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                     @RequestParam(value = "size",defaultValue = "10",required = false) int size,
                                     @RequestParam(required = false) Long categoryId,
                                     @RequestParam(required = false) String date,
                                     Model model) throws JsonProcessingException {
        Pageable pageRequest = PageRequest.of(page,size);
        Page<Product> pages = productService.getProductRanking(pageRequest, categoryId,date);
        model.addAttribute("products",pages);
        List<CategoryDto> cateList = productService.getCateCode().stream().map(CategoryDto::of).collect(Collectors.toList());
        // List<ProductCategory>를 JSON 형식으로 변환, sout 확인용
        //String cateJson = objectMapper.writeValueAsString(cateList);
        model.addAttribute("cateJson", cateList);
        model.addAttribute("date",date);
        model.addAttribute("size",size);

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

    @GetMapping("/addProduct")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        List<CategoryDto> cateList = productService.getCateCode().stream().map(CategoryDto::of).collect(Collectors.toList());
        // List<ProductCategory>를 JSON 형식으로 변환, sout 확인용
        //String cateJson = objectMapper.writeValueAsString(cateList);
        model.addAttribute("cateList", cateList);
        return "product/addProductForm";
    }

    @PostMapping("addProduct")
    public String addProduct(Product product){

        return "product/shopping";
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

    @PostMapping("/cancelCart")
    @ResponseBody
    public ResponseEntity<String> cancelCart(@RequestParam("cnoList") List<Long> cnoList) {
        productService.cancelCart(cnoList);
        // 예시로 성공적으로 취소되었다고 가정하고 OK 응답을 반환
        return ResponseEntity.ok("취소되었습니다.");
    }

    @GetMapping("/cancelOrder")
    public String cancelOrder(){

        return "/";
    }

    @GetMapping("/orderProduct")
    public String orderProduct(){
        return "/";
    }



}

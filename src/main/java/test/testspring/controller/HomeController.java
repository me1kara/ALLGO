package test.testspring.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import test.testspring.DTO.ProductDTO;
import test.testspring.domain.Product;
import test.testspring.service.ProductService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class HomeController {

    @Value("${server.url}")
    String url;

    ProductService productService;
    @Autowired
    public HomeController(ProductService productService){
        this.productService = productService;
    }
    @RequestMapping("/")
    public String home(Model model){
        System.out.println(url+"확인용"); //확인완료

        List<ProductDTO> products = productService.getMainProduct();
        model.addAttribute("products",products);
        return "home";
    }
    @PostMapping("/admin/login")
    public String login2(HttpServletRequest request){
        String referer = request.getHeader("Referer");
        return "home";
    }


//    @GetMapping("mypage")
//    public String mypage(){
//        return "members/mypage";
//    }


//    @GetMapping("/board")
//    public String board(){
//        return "board";
//    }
}

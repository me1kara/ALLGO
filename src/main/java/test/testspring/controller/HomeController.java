package test.testspring.controller;


import org.springframework.beans.factory.annotation.Autowired;
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
    ProductService productService;
    @Autowired
    public HomeController(ProductService productService){
        this.productService = productService;
    }
    @RequestMapping("/")
    public String home(Model model){
        List<ProductDTO> products = productService.getMainProduct();
        model.addAttribute("products",products);
        return "home";
    }

    @GetMapping("/admin")
    public String admin(){
        return "/admin/sample";
    }



    @PostMapping("/admin/login")
    public String login2(HttpServletRequest request){
        String referer = request.getHeader("Referer");
        System.out.println("Referer: " + referer);
        return "home";
    }

    @GetMapping("/haeder")
    public String haeder2(HttpServletRequest request){

        return "fragment/header";
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

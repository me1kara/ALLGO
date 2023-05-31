package test.testspring.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import test.testspring.domain.Member;
import test.testspring.service.MemberService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/admin")
    public String admin(){
        System.out.println("여기 들오냐?");
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

    @GetMapping("mypage")
    public String mypage(){
        return "members/mypage";
    }


//    @GetMapping("/board")
//    public String board(){
//        return "board";
//    }
}

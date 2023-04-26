package test.testspring.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import test.testspring.domain.Member;
import test.testspring.service.MemberService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
    @Autowired
    MemberService memberService;
    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("admin")
    public String admin(){
        return "/login/loginForm";
    }


//    @GetMapping("/board")
//    public String board(){
//        return "board";
//    }
}

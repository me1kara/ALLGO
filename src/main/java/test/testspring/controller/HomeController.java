package test.testspring.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpServletRequest;

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

//    @GetMapping("mypage")
//    public String mypage(){
//        return "members/mypage";
//    }


//    @GetMapping("/board")
//    public String board(){
//        return "board";
//    }
}

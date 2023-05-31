package test.testspring.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/help")
public class HelpController {
    @GetMapping("/question")
    public String viewMain(){
        return "/help/main";
    }

}

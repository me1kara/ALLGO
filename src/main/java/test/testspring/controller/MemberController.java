package test.testspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import test.testspring.domain.Member;
import test.testspring.service.MemberService;

import java.util.List;
import java.util.Optional;

@Controller
public class MemberController {

    private final MemberService memberService;

    //생성자를 통해 bean 주입
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/login")
    public String loginForm(){
        return "login/loginForm";
    }

    @GetMapping("/registerForm")
    public String registerForm(){
        return "login/registerForm";
    }

    @GetMapping("/members/new")
    public String createForm(){
        return "members/createMemberForm";
    }
    @PostMapping("/members/new")
    public String create(Member form){
        memberService.join(form);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList";
    }

    @GetMapping("/members/findId")
    public String findMember(){return "memebers/findMember";}

    @PostMapping("/checkId")
    @ResponseBody
    public String checkId(@RequestParam("id") String id){
        Optional<Member> member = memberService.findOne(id);
        return member.isPresent() ? "unable" : "able";
    }


}

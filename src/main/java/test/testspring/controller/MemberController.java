package test.testspring.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.testspring.domain.Member;
import test.testspring.security.SecurityController;
import test.testspring.security.SecurityService;
import test.testspring.service.EmailService;
import test.testspring.service.MemberService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class MemberController {



    private final MemberService memberService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private EmailService emailService;

    @Value("${coolSmsKey}")
    private String COOL_KEY;
    @Value("${coolSmsSecretKey}")
    private String COOL_SECRETKEY;

    //생성자를 통해 bean 주입
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("memberLogin")
    public String loginForm(){

        return "login/loginForm";
    }

    @PostMapping("memberLogin")
    public String checkMember(Model model, Member member, HttpServletResponse response){
        boolean result = memberService.isValidMember(member);
        //로그인 성공시
        if (result) {
            String token = securityService.createToken(member.getId(),member.getRole(),(2*1000*60));
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } else {
            // 로그인 실패 시
            model.addAttribute("id",member.getId());
            return "login/loginForm";
        }

        return "home";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/registerForm")
    public String registerForm(){
        return "login/registerForm";
    }

    @PostMapping("/registerCheck")
    public String joinRegister(){

        return "redirect:/";
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
        return member.isPresent() ? "unable" : "ok";
    }

    @PostMapping("/checkPhone")
    @ResponseBody
    public String checkPhone(@RequestParam("phone") String phone){
        Optional<Member> member = memberService.findOne(phone);

        Random rand  = new Random();
        String numStr = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr+=ran;
        }


        Message coolsms = new Message(COOL_KEY, COOL_SECRETKEY);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phone); // 수신
        params.put("from", phone); // 발신
        params.put("type", "SMS");
        params.put("text", "인증번호 "+numStr+" 를 입력하세요.");
        //arams.put("app_version", "test app 2.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
        return numStr;
    }

    @PostMapping("/checkEmail")
    @ResponseBody
    public String emailConfirm(@RequestParam String email) throws Exception {
        String confirm = emailService.sendSimpleMessage(email);
        return confirm;
    }

    @PostMapping("/joinMember")
    public String joinMember(Member member, @RequestParam String addressDetail){
        member.setAddress(member.getAddress()+addressDetail);
        memberService.join(member);
        return "redirect:/joinResult";
    }






}

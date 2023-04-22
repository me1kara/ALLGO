package test.testspring.controller;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.testspring.domain.Member;
import test.testspring.service.EmailService;
import test.testspring.service.MemberService;

import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
public class MemberController {

    private final MemberService memberService;
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

    @GetMapping("/login")
    public String loginForm(){

        return "login/loginForm";
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

        System.out.println("수신자 번호 : " + phone);
        System.out.println("인증번호 : " + numStr);

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
        System.out.println(confirm);
        return confirm;
    }




}

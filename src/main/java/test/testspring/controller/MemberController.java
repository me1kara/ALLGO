package test.testspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import test.testspring.DTO.CartDTO;
import test.testspring.domain.Cart;
import test.testspring.domain.Member;
import test.testspring.domain.Order;
import test.testspring.security.MemberDetailService;
import test.testspring.service.EmailService;
import test.testspring.service.MemberService;
import test.testspring.service.ProductService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @Autowired
    private MemberDetailService memberDetailService;
    private final ProductService productService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    //생성자를 통해 bean 주입
    @Autowired
    public MemberController(MemberService memberService, ProductService productService) {
        this.memberService = memberService;
        this.productService = productService;
    }


    //로그인시
    @GetMapping("/login")
    public String login(){

        return "login/loginForm";
    }
    @PostMapping("/login")
    public String validMember(HttpSession session, Model model, Member member, HttpServletResponse response){
//        boolean result = memberService.isValidMember(member);
//        //로그인 성공시
//        if (result) {
//            session.setAttribute("member", member);
//            /*
//            String token = securityService.createToken(member.getId(),member.getRole(),(2*1000*60));
//            Cookie cookie = new Cookie("jwt", token);
//            cookie.setHttpOnly(true);
//            response.addCookie(cookie);
//            */
//        } else {
//            // 로그인 실패 시
//            model.addAttribute("id",member.getId());
//            return "login/loginForm";
//        }

        return "home";
    }

    //회원가입
    @GetMapping("/registerForm")
    public String viewPostMember(){
        return "login/registerForm";
    }

    @PostMapping("/registerCheck")
    public String postMember(){

        return "redirect:/";
    }
//    @GetMapping("/member/new")
//    public String createForm(){
//        return "members/createMemberForm";
//    }
//    @PostMapping("/new")
//    public String create(Member form){
//        memberService.join(form);
//        return "redirect:/";
//    }

//    @GetMapping("")
//    public String list(Model model){
//        List<Member> members = memberService.findMembers();
//        model.addAttribute("members",members);
//        return "members/memberList";
//    }

    @GetMapping("/findId")
    public String findMemberForm(){return "login/findId";}

    @PostMapping("/checkId")
    @ResponseBody
    public String validId(@RequestParam("id") String id){
        Optional<Member> member = memberService.findOne(id);
        return member.isPresent() ? "unable" : "ok";
    }

    @PostMapping("/checkPhone")
    @ResponseBody
    public String validPhone(@RequestParam("phone") String phone){
        boolean phoneValid = memberService.findByPhone(phone)
                .isEmpty();
        //가입된 유저가 없다면
        if(phoneValid) {
            return memberService.sendToPhone(phone);
        }else return "no";
    }

    @PostMapping("/checkEmail")
    @ResponseBody
    public String validEmail(@RequestParam String email) throws Exception {
        boolean emailValid = memberService.findByEmail(email).isEmpty();
        //가입된 유저가 없다면
        if(emailValid) return emailService.sendSimpleMessage(email);
        else return "no";
    }

//    @PostMapping("/joinMember")
//    public String joinMember(Member member, @RequestParam String addressDetail){
//        member.setAddress(member.getAddress()+addressDetail);
//        memberService.join(member);
//        return "redirect:/joinResult";
//    }

    //아이디 패스워드 찾기
    @PostMapping("/sendToPhone")
    @ResponseBody
    public String findMemberByPhone(@RequestParam("phone") String phone){
        boolean phoneValid = memberService.findByPhone(phone)
                .isEmpty();
        //가입된 유저가 있다면
        if(!phoneValid) {
            return memberService.sendToPhone(phone);
        }else return "no";
    }
    @PostMapping("/sendToEmail")
    @ResponseBody
    public String findMemberByEmail(@RequestParam String email) throws Exception {
        boolean emailValid = memberService.findByEmail(email).isEmpty();
        //가입된 유저가 있다면
        if(!emailValid) return emailService.sendSimpleMessage(email);
        else return "no";
    }

    @PostMapping("/findAndSet")
    public String findAndSetMember(HttpServletRequest request, Model model) throws Exception {
        String emailParam = request.getParameter("email");
        String phoneParam = request.getParameter("phone");
        if(emailParam != null) {
            memberService.findByEmail(emailParam)
                    .ifPresent(member -> model.addAttribute("id", member.getId()));
        } else if(phoneParam != null) {
            memberService.findByPhone(phoneParam)
                    .ifPresent(member -> model.addAttribute("id", member.getId()));
        } else {
            throw new IllegalArgumentException("이메일, 휴대폰 번호 둘다 주어지지 않았습니다.");
        }


        return "login/findAndSet";
    }

    @PostMapping("/setId")
    @ResponseBody
    public String setId(@RequestParam("password") String password
                       ,@RequestParam("id") String id){
        memberService.modifyPw(id,password);
        return "ok";
    }

    @GetMapping("/myPage")
    @PostAuthorize("isAuthenticated()")
    public String viewMyPage(@AuthenticationPrincipal User user, HttpSession session, Model model, @RequestParam(value = "item",required = false) String item){
        // 사용자의 로그인 ID 가져오기
        String id = user.getUsername();

        if(item==null)item="cart";
        switch(item){
            case "cart" :
                List<CartDTO> cartList =  productService.getCartList(id);
                model.addAttribute("myCart", cartList);
                break;
            case "orderList" :
                List<Order> orderList = productService.getOrderList(id);
                model.addAttribute("orderList", orderList);
                break;
        }
        return "member/myPage";
    }


    @GetMapping("/myInformationList")
    public String viewMyInfo(Model model){
        // 현재 로그인된 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String id = authentication.getName();
        Member member = memberService.getMemberById(id);
        model.addAttribute("member",member);

        return "member/myInformationList";
    }


    @PostMapping("/modifyAddress")
    @ResponseBody
    public String putAddressInMember(@RequestParam("address") String address, @RequestParam("address2") String address2){
        // 현재 로그인된 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String id = authentication.getName();
        StringBuilder addressResult = new StringBuilder();
        addressResult.append(address).append(" ").append(address2);
        memberService.modifyAddress(addressResult.toString(),id);

        return "ok";
    }
    @PostMapping("/modifyEmail")
    @ResponseBody
    public String putEmailInMember(@RequestParam("email") String email){
        // 현재 로그인된 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String id = authentication.getName();

        memberService.modifyEmail(id,email);

        return "ok";
    }
    @PostMapping("/modifyPhone")
    @ResponseBody
    public String putPhoneInMember(@RequestParam("phone") String phone){
        // 현재 로그인된 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String id = authentication.getName();

        memberService.modifyPhone(id,phone);
        return "ok";
    }
    @PostMapping("/modifyPassword")
    @ResponseBody
    public String putPasswordInMember(String password, String originPassword){
        // 현재 로그인된 사용자의 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String id = authentication.getName();
        boolean res = memberService.modifyPassword(id,password,originPassword);
        if(res){
            return "ok";
        }else {
            return "false";
        }
    }

    @GetMapping("/modifyAddress")
    public String viewModifyAddressForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String id = authentication.getName();
        Member member = memberService.getMemberById(id);
        model.addAttribute("address",member.getAddress());
        return "/member/modifyForm";
    }
    @GetMapping("/modifyPhone")
    public String viewModifyPhoneForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String id = authentication.getName();
        Member member = memberService.getMemberById(id);
        model.addAttribute("phone",member.getPhone());

        return "/member/modifyForm";
    }
    @GetMapping("/modifyEmail")
    public String viewModifyEmailForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String id = authentication.getName();
        Member member = memberService.getMemberById(id);
        model.addAttribute("email",member.getEmail());


        return "/member/modifyForm";
    }
    @GetMapping("/modifyPassword")
    public String viewModifyPasswordForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 사용자의 로그인 ID 가져오기
        String id = authentication.getName();
        Member member = memberService.getMemberById(id);
        model.addAttribute("password",member.getPassword());
        return "/member/modifyForm";
    }

    //비번 분실시 고치는용도
    @GetMapping("/resetPass")
    public String putPasswordInAll(String name){

        String pass = passwordEncoder.encode("12345");
        Member member = memberService.getMemberById(name);
        member.setPassword(pass);

        memberService.modifyAll(member);
        return "redirect:/";
    }






}

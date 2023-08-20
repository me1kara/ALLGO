package test.testspring.controller;


import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import groovyjarjarpicocli.CommandLine;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import test.testspring.DTO.CategoryDto;
import test.testspring.DTO.HelpBoardDTO;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.HelpBoard;
import test.testspring.domain.HelpComment;
import test.testspring.domain.HelpImg;
import test.testspring.domain.Product;
import test.testspring.service.HelpService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/help")
public class HelpController {
    HelpService helpService;
    @Autowired
    public HelpController(HelpService helpService) {
        this.helpService = helpService;
    }
    @GetMapping("/question")
    public String viewMain(Model model, String listItem){
        model.addAttribute("listItem",listItem);
        return "/help/main";
    }

    @GetMapping("/myQuestionList")
    public String viewMyQuestionList(@RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                      @RequestParam(value = "size",defaultValue = "10",required = false) int size,
                                      SearchDTO search,
                                      Model model,
                                     String listItem) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        Pageable pageRequest = PageRequest.of(page,size);
        Page<HelpBoard> helps = helpService.findByMemberId(pageRequest,id,search.getKeyword());
        model.addAttribute("helps",helps);
        model.addAttribute("listItem",listItem);

        return "/help/myQuestionList";
    }



    @GetMapping("/write")
    public String insertHelp(){
        return "/help/writeForm";
    }
    @PostMapping("/write")
    public String insertHelp(HelpBoard help){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id = authentication.getName();
        help.setResolved(true);
        helpService.insertHelp(id,help);
        return "redirect:/help/questionList";
    }

    @GetMapping("/modify")
    public String modifyHelp(Model model, Long id){
        HelpBoard helpBoard = helpService.findOne(id);
        model.addAttribute("help",helpBoard);
        return "/help/modifyForm";
    }

    @PostMapping("/modify")
    public String modifyHelp(HelpBoard helpBoard){
        helpService.modifyHelp(helpBoard);
        Long id = helpBoard.getId();

        return "redirect:/help/questionContent?id="+id;
    }

    @GetMapping("/delete")
    public String deleteHelp(Model model, Long id){
        model.addAttribute("deleteNo",id);
        System.out.println(id);
        return "/help/deleteForm";
    }

    @PostMapping("/delete")
    public String deleteHelp(Long deleteNo){
        System.out.println(deleteNo+"확인용");
        helpService.deleteHelp(deleteNo);
        return "redirect:/help/questionList";
    }


    @GetMapping("/questionList")
    public String viewQuestionList(@RequestParam(value = "page",defaultValue = "0",required = false) int page,
                                   @RequestParam(value = "size",defaultValue = "10",required = false) int size,
                                   SearchDTO search,
                                   Model model) throws JsonProcessingException {
        Pageable pageRequest = PageRequest.of(page,size);
        Page<HelpBoard> helps = helpService.findAll(pageRequest, search);
        model.addAttribute("helps",helps);

        return "/help/questionList";
    }

    @GetMapping("/questionContent")
    public String viewQuestionContent(Model model, Long id,
                                      @RequestParam(value = "top", required = false) Double top) throws JsonProcessingException {
        HelpBoardDTO helpBoard = helpService.viewQuestionContent(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 사용자의 모든 권한 확인
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 특정 권한이 있는지 확인
        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            // 특정 권한에 따라 동작 수행

        if (isAdmin || helpBoard.getMember().getId().equals(authentication.getName())) {
            // 댓글쓰기 권한이 있는 경우(관리자 or 본인)
            model.addAttribute("replyRight", true);
        }


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonInString = mapper.writeValueAsString(helpBoard);
        model.addAttribute("help",jsonInString);
        model.addAttribute("helpId",helpBoard.getId());
        if(authentication.getName()!=null){
            if(helpBoard.getMember().getId().equals(authentication.getName())){
                model.addAttribute("right",true);
            }
        }
        model.addAttribute("top",top);

        return "/help/questionContent";
    }

    @PostMapping("/addComment")
    public String addComment(Model model,
                             HelpComment comment,
                             @RequestParam("position") double position) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String writerId = authentication.getName();
        comment.setMemberId(writerId);
        helpService.addComment(comment);
        return "redirect:/help/questionContent?id=" + comment.getBoardId() +"&top="+position;
    }



}

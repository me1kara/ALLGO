package test.testspring.controller;


import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
        HelpBoard helpBoard = helpService.viewQuestionContent(id);
        HelpBoardDTO hd = new HelpBoardDTO(helpBoard);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 현재 사용자의 모든 권한 확인
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        // 특정 권한이 있는지 확인
        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
            // 특정 권한에 따라 동작 수행
        if (isAdmin || helpBoard.getId().equals(authentication.getName())) {
            // 댓글쓰기 권한이 있는 경우(관리자 or 본인)
            model.addAttribute("replyRight", true);
        }


        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonInString = mapper.writeValueAsString(hd);

        System.out.println(jsonInString);

        model.addAttribute("help",jsonInString);
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

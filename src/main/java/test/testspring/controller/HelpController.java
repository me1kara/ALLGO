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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import test.testspring.DTO.CategoryDto;
import test.testspring.DTO.HelpBoardDTO;
import test.testspring.DTO.SearchDTO;
import test.testspring.domain.HelpBoard;
import test.testspring.domain.HelpImg;
import test.testspring.domain.Product;
import test.testspring.service.HelpService;

import java.util.ArrayList;
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
    public String viewQuestionContent(Model model, Long id) throws JsonProcessingException {
        HelpBoard helpBoard = helpService.viewQuestionContent(id);
        HelpBoardDTO hd = new HelpBoardDTO(helpBoard);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonInString = mapper.writeValueAsString(hd);
        model.addAttribute("help",jsonInString);
        System.out.println(jsonInString);

        return "/help/questionContent";

    }


}

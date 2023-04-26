package test.testspring.controller;


import org.springframework.stereotype.Controller;

@Controller
public class ItemController {


    public String showHotdeal(){

        return "itemList/hotDeal";
    }

    public String showAllItems(){

        return "itemList/AllItems";
    }

    public String showUsedItem(){

        return "itemList/usedItem";
    }

    public String showRanking(){

        return "itemList/ranking";
    }



}

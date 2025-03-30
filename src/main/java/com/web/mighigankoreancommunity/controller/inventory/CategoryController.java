package com.web.mighigankoreancommunity.controller.inventory;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/page/category")
@Controller
public class CategoryController {
    @GetMapping("/list")
    public String listCategory() {
        return "/restaurant/category-list";
    }

}

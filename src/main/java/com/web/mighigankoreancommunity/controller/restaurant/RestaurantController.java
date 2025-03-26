package com.web.mighigankoreancommunity.controller.restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/page/restaurant")
@Controller
public class RestaurantController {

    @GetMapping("/list")
    public String list() {
        return "restaurant/restaurant-list";
    }
}

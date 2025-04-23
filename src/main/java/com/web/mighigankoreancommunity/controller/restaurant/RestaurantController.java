package com.web.mighigankoreancommunity.controller.restaurant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/page/restaurant")
@Controller
public class RestaurantController {

    @GetMapping("/list")
    public String list(Model model, HttpServletRequest request) {
        return "owner-pages/restaurant-list";
    }

}

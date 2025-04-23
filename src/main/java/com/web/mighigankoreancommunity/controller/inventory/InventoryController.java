package com.web.mighigankoreancommunity.controller.inventory;

import com.web.mighigankoreancommunity.domain.MemberRole;
import com.web.mighigankoreancommunity.entity.userDetails.CustomUserDetails;
import com.web.mighigankoreancommunity.service.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RequestMapping("/page/inventory")
@Controller
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/list")
    public String inventoryList(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestParam Long restaurantId) {
        return "owner-pages/inventory-list";
    }

    @GetMapping("/category")
    public String category() {
        return "owner-pages/category-list";
    }
}

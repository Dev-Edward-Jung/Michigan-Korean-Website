package com.web.mighigankoreancommunity.controller.inventory;

import ch.qos.logback.core.model.Model;
import com.web.mighigankoreancommunity.entity.Inventory;
import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.*;

@RequiredArgsConstructor
@RequestMapping("/page/inventory")
@Controller
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/list")
    public String inventoryList(Model model) {
        return "restaurant/inventory-list";
    }
}

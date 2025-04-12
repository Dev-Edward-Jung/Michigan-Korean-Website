package com.web.mighigankoreancommunity.controller.inventory;

import ch.qos.logback.core.model.Model;
import com.web.mighigankoreancommunity.service.inventory.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/page/inventory")
@Controller
public class InventoryController {
    private final InventoryService inventoryService;

    @GetMapping("/list")
    public String inventoryList(Model model) {
        return "restaurant/inventory-list";
    }

    @GetMapping("/category")
    public String category() {
        return "restaurant/category-list";
    }
}

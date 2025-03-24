package com.web.mighigankoreancommunity.controller.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/page/inventory")
@Controller
public class InventoryController {
    @GetMapping("/goinventory")
    public String goInventory() {
        return "restaurant/inventory-list";
    }
}

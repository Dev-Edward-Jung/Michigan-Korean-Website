package com.web.mighigankoreancommunity.controller.inventory;


import com.web.mighigankoreancommunity.entity.Member;
import com.web.mighigankoreancommunity.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/inventory")
@RestController
public class InventoryRestController {

    @PostMapping("/inventoryList")
    public String inventoryList() {
        return null;
    }
}

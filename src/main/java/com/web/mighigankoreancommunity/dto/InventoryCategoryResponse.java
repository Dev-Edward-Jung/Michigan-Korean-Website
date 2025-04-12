package com.web.mighigankoreancommunity.dto;


import com.web.mighigankoreancommunity.entity.Category;
import com.web.mighigankoreancommunity.entity.Inventory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InventoryCategoryResponse {
    private List<Category> categoryList;
    private List<Inventory> inventoryList;
}

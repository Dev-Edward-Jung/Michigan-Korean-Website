package com.web.mighigankoreancommunity.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryDTO {
    private Long id;
    private Long restaurantId;
    private String name;


}

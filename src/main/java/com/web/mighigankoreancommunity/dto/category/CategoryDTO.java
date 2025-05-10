package com.web.mighigankoreancommunity.dto.category;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Schema(description = "Request and Response for Category")
public class CategoryDTO {
    private Long id;
    private Long restaurantId;
    private String name;


}

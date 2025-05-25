package com.web.mighigankoreancommunity.dto.category;


import com.web.mighigankoreancommunity.entity.Category;
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

    // Entity → DTO
    public static CategoryDTO from(Category entity) {
        return CategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .restaurantId(
                        entity.getRestaurant() != null ? entity.getRestaurant().getId() : null
                )
                .build();
    }

}

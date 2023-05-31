package test.testspring.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import test.testspring.domain.ProductCategory;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long categoryId;
    private String categoryName;
    private int tier;
    private List<CategoryDto> children;
    public static CategoryDto of(ProductCategory category) {
        return new CategoryDto(
                category.getCateCode(),
                category.getCateName(),
                category.getTier(),
                category.getChildren().stream().map(CategoryDto::of).collect(Collectors.toList())
        );
    }
}

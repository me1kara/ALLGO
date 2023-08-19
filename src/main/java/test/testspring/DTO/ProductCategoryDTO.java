package test.testspring.DTO;


import lombok.Getter;
import lombok.Setter;
import test.testspring.domain.ProductCategory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class ProductCategoryDTO {

    private Long cateCode;
    private String cateName;
    private int tier;
    private ProductCategory cateParent;
    // 자식 정의
    private List<ProductCategoryDTO> children = new ArrayList<>();
}

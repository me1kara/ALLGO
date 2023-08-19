package test.testspring.DTO;


import lombok.Getter;
import lombok.Setter;
import test.testspring.domain.Card;
import test.testspring.domain.Member;
import test.testspring.domain.ProductCategory;
import test.testspring.domain.ProductImg;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ProductDTO {
    private Long product_no;
    private String product_name;
    private String product_content;
    private BigDecimal price;
    private Float discount_rate;
    private Integer amount;
    private LocalDateTime registration_at;
    private int favorite;
    private int view;
    private Card card;
    private List<ProductImg> productImgs = new ArrayList<>();
    private ProductCategory productCategory;

    private Member member;

}

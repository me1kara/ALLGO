package test.testspring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long product_no;
    private String seller_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    private String product_name;

    private String product_content;

    private BigDecimal price;

    private Float discount_rate;
    private Integer amount;
    private LocalDateTime registration_at;
    private int favorite;
    private int view;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private final List<ProductImg> productImgs = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="cate_code")
    private ProductCategory productCategory;
    @OneToOne
    @JoinColumn(name="seller_id", insertable=false, updatable = false)
    private Member member;

}

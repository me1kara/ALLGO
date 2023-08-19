package test.testspring.domain;

import lombok.*;

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
    private String product_name;
    private String product_content;
    private BigDecimal price;
    private Float discount_rate;
    private Integer amount;
    @Column(name="registration_at")
    private LocalDateTime registration_at;

    @Column(name="favorite", nullable = true)
    private int favorite;

    @Column(name="view", nullable = true)
    private int view;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="pid")
    private List<ProductImg> productImgs = new ArrayList<>();
    @OneToOne
    @JoinColumn(name="cate_code")
    private ProductCategory productCategory;
    @OneToOne
    @JoinColumn(name="seller_id", updatable = false)
    private Member member;

}

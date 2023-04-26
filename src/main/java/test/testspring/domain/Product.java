package test.testspring.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
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
    @Builder
    public Product(String sellerId, Card card, String productName, String productContent, BigDecimal price, Float discountRate, Integer amount) {
        this.seller_id = sellerId;
        this.card = card;
        this.product_name = productName;
        this.product_content = productContent;
        this.price = price;
        this.discount_rate = discountRate;
        this.amount = amount;
    }


}

package test.testspring.domain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="imp_uid")
    private String imp_uid;
    @Column(name="merchant_uid")
    private String merchant_uid;
    @Column(name = "trade_amount")
    private int trade_amount;
    @Column(name = "total_price")
    private BigDecimal total_price;
    @Column(name = "payment_at")
    @Builder.Default
    private LocalDateTime payment_at = LocalDateTime.now();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Member seller;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Member buyer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no")
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="card_id")
//    private Card card;

}
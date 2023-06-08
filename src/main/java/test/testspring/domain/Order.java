package test.testspring.domain;
import lombok.AllArgsConstructor;
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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "seller_id")
    private String seller_id;
    @Column(name = "buyer_id")
    private String buyer_id;

    @Column(name = "product_no")
    private Long product_no;

    @Column(name = "trade_amount")
    private int trade_amount;

    @Column(name = "total_price")
    private BigDecimal total_price;

    @Column(name = "payment_at")
    private LocalDateTime payment_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", insertable = false, updatable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", insertable = false, updatable = false)
    private Member buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_no", insertable = false, updatable = false)
    private Product product;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

}
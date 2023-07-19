package test.testspring.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import test.testspring.domain.Card;
import test.testspring.domain.Delivery;
import test.testspring.domain.Order;
import test.testspring.domain.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductDeliveryDTO {
    private String product_name;
    private Long product_no;
    private int trade_amount;
    private BigDecimal price;
    private BigDecimal amount;
    private String address;
    private List<Card> cards = new ArrayList<>();
    private Delivery delivery;
    private float discount_rate;

    public OrderProductDeliveryDTO(Product product, List<Card> cards, Delivery delivery, int trade_amount) {
        this.discount_rate = product.getDiscount_rate();
        this.product_no = product.getProduct_no();
        this.product_name = product.getProduct_name();
        this.trade_amount = trade_amount;
        this.price = product.getPrice();
        this.address = product.getMember().getAddress();
        this.cards = cards;
        this.delivery = delivery;
    }

}

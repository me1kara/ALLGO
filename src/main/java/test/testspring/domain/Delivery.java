package test.testspring.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="delivery_id")
    private Long delivery_id;
    @Column(name="order_id")
    private Long order_id;
    @Column(name="delivery_date")
    private Date delivery_date;
    @Column(name="delivery_status")
    private String delivery_status;

    @Column(name="delivery_address")
    private String delivery_address;

    @Column(name="delivery_person")
    private String delivery_person;

    @Column(name="delivery_phone")
    private String delivery_phone;

}

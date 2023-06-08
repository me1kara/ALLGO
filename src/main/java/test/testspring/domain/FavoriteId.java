package test.testspring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteId implements Serializable {
    private String member_id;

    private Long product_no;

    // Getter and Setter methods
    // ...
}



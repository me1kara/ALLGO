package test.testspring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteId implements Serializable {
    private String member_id;
    private Long product_no;
    private Date created_at;


    // Getter and Setter methods
    // ...
}



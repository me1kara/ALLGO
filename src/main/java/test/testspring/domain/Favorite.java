package test.testspring.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@Table(name = "favorite")
@IdClass(FavoriteId.class)
@NoArgsConstructor
public class Favorite implements Serializable {

    @Id
    @Column(name = "member_id")
    private String member_id;

    @Id
    @Column(name = "product_no")
    private Long product_no;

    // Getter and Setter methods
    // ...
}


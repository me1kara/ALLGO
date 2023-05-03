package test.testspring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "product_category")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ProductCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cate_code")
    private Long cateCode;
    @Column(name = "cate_name", nullable = false)
    private String cateName;
    private int tier;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cate_parent", referencedColumnName = "cate_code", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProductCategory cateParent;

}


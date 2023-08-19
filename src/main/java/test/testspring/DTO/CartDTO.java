package test.testspring.DTO;

import lombok.Getter;
import lombok.Setter;
import org.apache.velocity.tools.config.SkipSetters;
import org.modelmapper.ModelMapper;
import test.testspring.domain.Product;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
public class CartDTO {
    private Long cid;
    private String mid;
    private int pCount;
    private Date keep_at;
    private ProductDTO product;

}

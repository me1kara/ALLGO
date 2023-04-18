package test.testspring.vo;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@Builder
@ToString
public class memberVO {
    private String id;
    private String name;
    private String password;
    private String jumin;
    private Date join_date;
    private String address;
    private String phone;
}

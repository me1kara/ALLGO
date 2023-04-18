package test.testspring.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity
@Table(name="board")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bno;
    @Column(name="title")
    private String title;
    private String writer;
    private String id;
    private Date regDate;
    private String content;
    private String photo;
    private String type;

}

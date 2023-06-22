package test.testspring.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@Table(name = "help_board")
public class HelpBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private boolean resolved;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Member member;

    @OneToMany(mappedBy = "helpBoard", cascade = CascadeType.ALL)
    private List<HelpImg> imgList = new ArrayList<>();

    @OneToMany(mappedBy = "helpBoard", cascade = CascadeType.ALL)
    private List<HelpComment> comments = new ArrayList<>();


    @Override
    public String toString() {
        return "HelpBoard{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", resolved=" + resolved +
                ", member=" + member.getId() +
                ", imgList=" + imgList.size() +
                ", comments=" + comments.size() +
                '}';
    }


}

package test.testspring.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "type")
    private String type;

    @Column(name = "no")
    private String no;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uid", insertable = false, updatable = false)
    private Member member;
    @Builder
    public Card(String uid, String type, String no, LocalDateTime endAt) {
        this.uid = uid;
        this.type = type;
        this.no = no;
        this.createdAt = LocalDateTime.now();
        this.endAt = endAt;
    }
}

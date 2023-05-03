package test.testspring.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="profileImg")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;
    private String uid;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="uid", insertable = false, updatable = false)
    private Member member;

}

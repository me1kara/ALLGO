package test.testspring.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "helpimg")
public class HelpImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "hid", nullable = false)
    private Long helpId;
    private String url;
    @ManyToOne
    @JoinColumn(name = "hid", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnore
    private HelpBoard helpBoard;

    // Getters and Setters
}

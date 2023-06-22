package test.testspring.domain;

import org.springframework.lang.Nullable;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Member {
    @Id
    private String id;
    @Nullable
    private String name;
    @Nullable
    private String email;
    @Nullable
    private String password;
    @Nullable
    private String address;
    @Nullable
    private String phone;
    @Nullable
    private boolean agreed_terms;
    @Nullable
    private boolean enabled;
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Card> cards;
    @ManyToMany
    @JoinTable(
            name ="member_role",
            joinColumns = @JoinColumn(name="member_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @Builder
    public Member(String id, String name, String email, String password, String phone, boolean agreed_terms, String address){
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.agreed_terms = agreed_terms;
        this.address = address;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Date created_at;
    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", agreed_terms=" + agreed_terms +
                ", enabled=" + enabled +
                ", created_at=" + created_at +
                '}';
    }

}

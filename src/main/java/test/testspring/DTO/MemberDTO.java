package test.testspring.DTO;

import edu.emory.mathcs.backport.java.util.AbstractList;
import lombok.Data;
import test.testspring.domain.Member;
import test.testspring.domain.Role;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;

    private List<Role> roles = new ArrayList<>();
    public MemberDTO(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.address = member.getAddress();
        this.roles = member.getRoles();
    }

    // Getters and Setters (if needed)
}


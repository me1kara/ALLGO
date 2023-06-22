package test.testspring.DTO;

import lombok.Data;
import test.testspring.domain.Member;

@Data
public class MemberDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
    public MemberDTO(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.email = member.getEmail();
        this.phone = member.getPhone();
        this.address = member.getAddress();
    }

    // Getters and Setters (if needed)
}


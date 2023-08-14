package test.testspring.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import test.testspring.domain.Member;
import test.testspring.domain.Role;

import java.util.ArrayList;
import java.util.Collection;

public class MemberDetail implements UserDetails {

    private Member member;

    public MemberDetail(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auths = new ArrayList<>();

        for(Role role: member.getRoles()){
            auths.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return role.getName();
                }
            });
        }
        return auths;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getId();
    }

    
    //계정 만료여부
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    // 계정 비활성화 여부

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 인증 정보를 항상 저장할지에 대한 여부

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }


    //계정 활성화 여부
    @Override
    public boolean isEnabled() {
        return true;
    }
}

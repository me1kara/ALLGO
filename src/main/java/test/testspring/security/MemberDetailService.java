package test.testspring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import test.testspring.domain.Member;
import test.testspring.domain.Role;
import test.testspring.repository.MemberRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;


@Service
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Autowired
    public MemberDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username으로 Repository에서 Member을 찾는 method
        Member member = memberRepository.findById(username).orElseThrow(()->new UsernameNotFoundException("계정을 찾을 수 없습니다."));

        ArrayList<GrantedAuthority> auths = new ArrayList<>();

        for(Role role: member.getRoles()){
            auths.add(new GrantedAuthority() {
                @Override
                public String getAuthority() {
                    return role.getName();
                }
            });
        }
        User user = new User(username,member.getPassword(), auths);
        return user;
    }
}

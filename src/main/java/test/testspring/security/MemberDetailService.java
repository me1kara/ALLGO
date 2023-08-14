package test.testspring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import test.testspring.domain.Member;
import test.testspring.repository.MemberRepository;


@Service
public class MemberDetailService implements UserDetailsService {


    private final MemberRepository memberRepository;
    @Autowired
    public MemberDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username으로 Repository에서 Member을 찾는 method
        Member member = memberRepository.findById(username).get();
        if(member == null)
            throw new UsernameNotFoundException("계정을 찾을 수 없습니다.");
        return new MemberDetail(member);
    }
}

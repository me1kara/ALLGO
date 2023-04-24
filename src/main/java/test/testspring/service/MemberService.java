package test.testspring.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.testspring.domain.Member;
import test.testspring.repository.MemberRepository;
import test.testspring.security.SecurityService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final SecurityService securityService;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public MemberService(MemberRepository memberRepository, SecurityService securityService, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.securityService = securityService;
        this.passwordEncoder = passwordEncoder;
    }


    public String join(Member member){
        long start = System.currentTimeMillis();
        try {
            //같은 이름이 있는 중복 회원x
            validateDuplicateMember(member);
            //member.setPassword(member.getPassword());
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            memberRepository.save(member);
            return member.getId();
        }finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("join = " + timeMs + "ms");
        }
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findById(member.getId())
                .ifPresent(m ->{
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });

    }



    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(String id){
        return memberRepository.findById(id);
    }

    public Boolean isValidMember(Member member) {
        Optional<Member> memberDB = memberRepository.findById(member.getId());
        if(memberDB.isPresent()){
            return passwordEncoder.matches(member.getPassword(),memberDB.get().getPassword());
        }else {
            return false;
        }
    }
}

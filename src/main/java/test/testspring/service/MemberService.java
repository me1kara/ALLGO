package test.testspring.service;

import lombok.Setter;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.testspring.domain.Member;
import test.testspring.domain.Role;
import test.testspring.repository.MemberRepository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Transactional
@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    @Value("${coolSmsKey}")
    private String COOL_KEY;
    @Value("${coolSmsSecretKey}")
    private String COOL_SECRETKEY;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public String join(Member member){
        long start = System.currentTimeMillis();
        try {
            //같은 이름이 있는 중복 회원x
            validateDuplicateMember(member);
            //member.setPassword(member.getPassword());
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            member.setEnabled(true);
            Role role = new Role();
            role.setId(2l);
            member.getRoles().add(role);
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

    public Optional<Member> findByEmail(String email){
        return memberRepository.findByEmail(email);
    }
    public Optional<Member> findByPhone(String phone){
        return memberRepository.findByPhone(phone);
    }

    public String sendToPhone(String phone) {
        Random rand = new Random();
        String numStr = "";
        for (int i = 0; i < 4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        Message coolsms = new Message(COOL_KEY, COOL_SECRETKEY);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phone); // 수신
        params.put("from", phone); // 발신
        params.put("type", "SMS");
        params.put("text", "인증번호 " + numStr + " 를 입력하세요.");
        //arams.put("app_version", "test app 2.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
        return numStr;
    }
    public void modifyPw(String id, String password) {
        Member member = memberRepository.findById(id).get();
        String encodePassword = passwordEncoder.encode(password);
        member.setPassword(encodePassword);
        member = memberRepository.save(member);
        Assert.assertEquals(encodePassword,member.getPassword());

    }

    public Member findById(String id) {
        return memberRepository.findById(id).orElse(Member.builder().build());
    }
}

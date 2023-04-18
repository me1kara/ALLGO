package test.testspring.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import test.testspring.domain.Member;
import test.testspring.service.MemberService;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assumptions.assumeThat;

@SpringBootTest
@Transactional
public class MemoryServiceIntegrationTest {
    @Autowired
    MemberRepository repository;
    @Autowired
    MemberService memberService;
    //@AfterEach
    //public void afterEach(){
        //repository.clearStore();
    //}

    @Test
    public void save(){
        Member member = new Member();
        member.setName("spring");

        repository.save(member);
        Member result = repository.findById(member.getId()).get();
        assumeThat(member).isEqualTo(result);

    }

    @Test
    public void findByName(){
        Member member1 = new Member();
        member1.setName("spring");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring2").get();
        assumeThat(result).isEqualTo(member1);

    }

    @Test
    public void findAll(){
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        assumeThat(result).isEqualTo(member1);


    }
}

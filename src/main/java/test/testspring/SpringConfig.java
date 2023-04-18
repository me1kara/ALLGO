package test.testspring;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import test.testspring.repository.JpaMemberRepository;
import test.testspring.repository.MemberRepository;
import test.testspring.service.MemberService;

import javax.persistence.EntityManager;


@Configuration
public class SpringConfig {
    /*private EntityManager em; //엔티티매니저는 본래 @persistContext 애너테이션을 사용해야하나 spring에서 알아서 di해줌

    public SpringConfig(EntityManager em) {
        this.em = em;
    }*/
    MemberRepository memberRepository;
    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository);
    }
/*    @Bean
    public MemberRepository memberRepository(){
        return new MemberRepository();
    }*/

/*
    @Bean
    public TimeTraceAop timeTraceAop(){
        return new TimeTraceAop();
    }
*/



}

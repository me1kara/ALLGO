package test.testspring.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Properties;


@Configuration
public class SpringConfig {
    /*private EntityManager em; //엔티티매니저는 본래 @persistContext 애너테이션을 사용해야하나 spring에서 알아서 di해줌

    public SpringConfig(EntityManager em) {
        this.em = em;
    }*/
//    MemberRepository memberRepository;
//    SecurityService securityService;
//    PasswordEncoder passwordEncoder;
//    @Autowired
//    public SpringConfig(MemberRepository memberRepository, SecurityService securityService, PasswordEncoder passwordEncoder) {
//        this.memberRepository = memberRepository;
//        this.securityService =securityService;
//        this.passwordEncoder = passwordEncoder;
//    }
//    @Bean
//    public MemberService memberService(){
//        return new MemberService(memberRepository, securityService, passwordEncoder);
//    }
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

    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.smtp.socketFactory.port}")
    private int socketPort;
    @Value("${mail.smtp.auth}")
    private boolean auth;
    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;
    @Value("${mail.smtp.starttls.required}")
    private boolean startlls_required;
    @Value("${mail.smtp.socketFactory.fallback}")
    private boolean fallback;
    @Value("${AdminMail.id}")
    private String id;
    @Value("${AdminMail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername(id);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }
    private Properties getMailProperties()
    {
        Properties pt = new Properties();
        pt.put("mail.smtp.socketFactory.port", socketPort);
        pt.put("mail.smtp.auth", auth);
        pt.put("mail.smtp.starttls.enable", starttls);
        pt.put("mail.smtp.starttls.required", startlls_required);
        pt.put("mail.smtp.socketFactory.fallback",fallback);
        pt.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return pt;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

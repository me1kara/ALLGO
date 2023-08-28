package test.testspring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import test.testspring.security.MemberDetailService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

//시큐리티 설정정하기
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    private final UserDetailsService memberDetailService;

    @Autowired
    public SecurityConfig(MemberDetailService memberDetailService) {
        this.memberDetailService = memberDetailService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests((requests) -> requests
                        // admin 페이지의 접속시 접속권한 admin 필요
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        //상품 구입 위해선 로그인 필요
                        .antMatchers("/product/buyProduct").authenticated()
                        //나머지는 허용
                        .anyRequest().permitAll())
                //로그인 폼 핸들링, 인증권한 필요한 경로로 접속
                .formLogin((form) -> form
                        .loginPage("/member/login")
                        //id와 password를 요구
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .failureHandler( // 로그인 실패 후 핸들러
                                new AuthenticationFailureHandler() { // 익명 객체 사용
                                    @Override
                                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                                        response.sendRedirect("/member/login?error");
                                    }
                                })
                        .permitAll()
                )

                .logout((logout) -> logout
                        .logoutUrl("/member/logout") // 로그아웃 URL 설정
                        .logoutSuccessUrl("/") // 로그아웃 후 리다이렉트할 URL
                        .permitAll()
                );
    }

    //로그인 시 id와 password 유효한지 확인


//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth)
//            throws Exception {
//
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                //입력한 비밀번호를 암호화해서 전달
//               .passwordEncoder(passwordEncoder())
//                //id password 확인
//                .usersByUsernameQuery("select id,password,enabled "
//                        + "from member "
//                        + "where id = ?")
//                //권한 조회
//                .authoritiesByUsernameQuery("select m.id, r.name "
//                        + "from member_role mr inner join member m on mr.member_id = m.id "
//                        + "inner join role r on mr.role_id = r.id "
//                        + "where m.id = ?");
//
//    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.
                userDetailsService(memberDetailService).passwordEncoder(passwordEncoder());
    }
    //인코더 방식을 빈을 통해 주입받음
    @Autowired
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

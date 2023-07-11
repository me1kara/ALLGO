package test.testspring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests((requests) -> requests
                        .antMatchers("/admin/**").hasRole("ADMIN")
                        .antMatchers("/product/buyProduct").authenticated()
                        .anyRequest().permitAll()
                )

                .formLogin((form) -> form
                        .loginPage("/member/login")
                        .loginProcessingUrl("/member/login")
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

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {

        auth.jdbcAuthentication()
                .dataSource(dataSource)
               .passwordEncoder(passwordEncoder())
                .usersByUsernameQuery("select id,password,enabled "
                        + "from member "
                        + "where id = ?")
                .authoritiesByUsernameQuery("select m.id, r.name "
                        + "from member_role mr inner join member m on mr.member_id = m.id "
                        + "inner join role r on mr.role_id = r.id "
                        + "where m.id = ?");

    }

    @Autowired
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}

package hello.security.config;

import hello.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    // 인증을 무시할 경로들을 설정
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/static/img/**", "/h2-console/**");
    }

    //http 관련 인증 설정이 가능
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/login", "/signup", "/user").permitAll() // 누구나 다 접근 가능
                    .antMatchers("/").hasRole("USER")       // USER, ADMIN만 접근 가능
                    .antMatchers("/admin").hasRole("ADMIN") // ADMIN 만 가능
                    .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에ㅐ 상관 없이 권한이 있어야 접근 가능
                .and()
                    .formLogin()
                        .loginPage("/login") // 로그인 페이지 링크
                        .defaultSuccessUrl("/") // 로그인 성공 후 리다이렉트 주소
                .and()
                    .logout()
                        .logoutSuccessUrl("/login") // 로그아웃 성공시 리다이렉트 주소
                        .invalidateHttpSession(true) // 세션 없애기
                ;
    }

    // 로그인할 때 필요한 정보를 가져오는 곳
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //유저 정보를 가져오는 서비스를 userService 로 지정합니다.
        auth.userDetailsService(userService)
                // 해당 서비스(userService)에서는 UserDetailsService를 implements해서
                // loadUserByUsername() 메서드 -> 서비스 참고
                .passwordEncoder(new BCryptPasswordEncoder());
        //패스워드 인코더는 passwordEncoder()를 사용합니다. (BCrypt)
    }
}

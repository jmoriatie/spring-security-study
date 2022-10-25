package hello.security.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class UserInfo implements UserDetails {

    @Id
    @Column(name = "code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "auth")
    private String auth;

    @Builder
    public UserInfo(String email, String password, String auth) {
        this.email = email;
        this.password = password;
        this.auth = auth;
    }


//    getAuthorities() : 이 메소드는 사용자의 권한을 콜렉션 형태로 반환해야하고,
//    콜렉션의 자료형은 무조건적으로 GrantedAuthority를 구현해야함
//    여기서는 권한이 중복되면 안되기 때문에 Set<GrantedAuthority>을 사용

//    ADMIN은 관리자의 권한(ADMIN)뿐만 아니라 일반 유저(USER)의 권한도 가지고 있기 때문에,
//    ADMIN의 auth는 "ROLE_ADMIN,ROLE_USER"와 같은 형태로 전달이 될 것이고,
//    쉼표(,) 기준으로 잘라서 ROLE_ADMIN과 ROLE_USER를 roles에 추가

    // 사용자의 권한을 콜렉션 형태로 반환
    // 단, 클래스 자료형은 GrantedAuthority를 구현해야함
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> roles = new HashSet<>();
        for(String role: auth.split(",")){
            roles.add(new SimpleGrantedAuthority(role));
        }
        return roles;
    }

    // 사용자의 id를 반환 (unique한 값)
    @Override
    public String getUsername() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료되었는지 확인하는 로직 넣는 곳
        return true; // true -> 만료되지 않음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금되어있는지 확인하는 로직
        return true; /// true -> 잠금되지 않음
    }

    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드 만료 확인 로직
        return true; // 만료되지 않음
    }

    // 계정 사용 가능 여부 반환
    @Override
    public boolean isEnabled() {
        // 계정 사용 가능한지 확인하는 로직
        return true; // 사용가능
    }
}

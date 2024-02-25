package com.example.shoppingmall.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    @Getter
    private String nickname;
    @Getter
    private String name;
    @Getter
    private String age;
    @Getter
    private String email;
    @Getter
    private String phone;
    @Getter
    private String businessNumber;
    private String authorities;

    public boolean inactiveToUser() {
        return nickname != null && name != null
                && age != null && email != null && phone != null;
    }

    // 사업자 번호를 가지고 있어야 사업자 사용자 전환
    public boolean userToBusiness() {
        return businessNumber != null;
    }

    public String getRawAuthorities() {
        return this.authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities  = new ArrayList<>();
        if (this.authorities != null) {
            String[] rawAuthorities = authorities.split(",");
            for (String rawAuthority : rawAuthorities) {
                // SimpleGrantedAuthority:Spring Security 내부에서 보통 String 형식의 권한을 표현하는 방식
                grantedAuthorities.add(new SimpleGrantedAuthority(rawAuthority));
            }
        }
        return grantedAuthorities;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

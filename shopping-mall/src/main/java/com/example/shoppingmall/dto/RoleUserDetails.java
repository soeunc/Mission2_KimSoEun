package com.example.shoppingmall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@NoArgsConstructor
@AllArgsConstructor
// 사용자들의 권한에 필요한 정보를 담는 곳
public class RoleUserDetails implements UserDetails {
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


    // 일반 사용자가 되기 위한 서비스를 가지고 있다면
    public boolean inactiveToUser() {
        return nickname != null && name != null
                && age != null && email != null && phone != null;
    }

    // 사업자 번호를 가지고 있고, 일반 사용자만이 사업자 사용자로 전환 가능
    public boolean userToBusiness() {
        return businessNumber != null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}

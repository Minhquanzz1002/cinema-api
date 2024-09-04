package vn.edu.iuh.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.UserStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private UUID id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private UserStatus status;

    public static UserPrincipal create(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                Collections.singleton(authority),
                user.getStatus()
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != UserStatus.SUSPENDED && status != UserStatus.BANNED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status == UserStatus.ACTIVE;
    }
}

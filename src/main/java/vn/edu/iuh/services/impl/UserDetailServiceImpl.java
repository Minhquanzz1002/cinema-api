package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.repositories.UserRepository;
import vn.edu.iuh.security.UserPrincipal;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng nào có email là " + email));
        if (user.getStatus() == UserStatus.DELETED) {
            throw new UsernameNotFoundException("Tài khoản đã  bị xóa vĩnh viễn");
        }

        return UserPrincipal.create(user);
    }
}

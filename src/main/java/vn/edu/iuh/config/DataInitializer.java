package vn.edu.iuh.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.edu.iuh.models.Movie;
import vn.edu.iuh.models.Role;
import vn.edu.iuh.models.User;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.repositories.MovieRepository;
import vn.edu.iuh.repositories.RoleRepository;
import vn.edu.iuh.repositories.UserRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("ROLE_ADMIN"));
            Role roleClient = roleRepository.save(new Role("ROLE_CLIENT"));

            if (userRepository.count() == 0) {
                userRepository.save(User.builder()
                        .name("Lê Hữu Bằng")
                        .phone("0837699806")
                        .birthday(LocalDate.of(2002, 10, 10))
                        .email("huubangle20002@gmail.com")
                        .password(passwordEncoder.encode("Cinema123123@"))
                        .gender(true)
                        .role(roleClient)
                        .status(UserStatus.ACTIVE)
                        .build());
            }
        }
        if (movieRepository.count() == 0) {
            movieRepository.save(
                    Movie.builder()
                            .code("MV000000001")
                            .title("Làm Giàu Với Ma")
                            .duration(112)
                            .summary("")
                            .age(16)
                            .country("Việt Nam")
                            .rating(0)
                            .imageLandscape("")
                            .producer("Bluebells Studios")
                            .slug("lam-giau-voi-ma")
                            .imagePortrait("")
                            .trailer("https://www.youtube.com/watch?v=2DmOv-pM1bM&t=2s")
                            .build()
            );
            movieRepository.save(
                    Movie.builder()
                            .code("MV000000002")
                            .title("Deadpool & Wolverine")
                            .duration(127)
                            .summary("")
                            .age(18)
                            .country("Mỹ")
                            .rating(0)
                            .imageLandscape("")
                            .producer("20th Century Studios")
                            .slug("lam-giau-voi-ma")
                            .imagePortrait("")
                            .trailer("https://www.youtube.com/watch?v=2DmOv-pM1bM&t=2s")
                            .build()
            );
        }
    }
}

package vn.edu.iuh.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.edu.iuh.models.*;
import vn.edu.iuh.models.enums.BaseStatus;
import vn.edu.iuh.models.enums.MovieStatus;
import vn.edu.iuh.models.enums.UserStatus;
import vn.edu.iuh.repositories.*;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final DirectorRepository directorRepository;
    private final ProducerRepository producerRepository;
    private final CinemaRepository cinemaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (cinemaRepository.count() == 0) {
            cinemaRepository.save(
                    Cinema.builder()
                            .name("Galaxy Trường Chinh")
                            .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                            .ward("Tây Thạnh")
                            .district("Tân Phú")
                            .city("Thành phố Hồ Chí Minh")
                            .hotline("19002224")
                            .images(List.of("image 1", "image 2"))
                            .build()
            );
        }

        if (roleRepository.count() == 0) {
            Role roleAdmin = roleRepository.save(new Role("ROLE_ADMIN"));
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

                userRepository.save(User.builder()
                        .name("Nguyễn Minh Quân")
                        .phone("0354927402")
                        .birthday(LocalDate.of(2002, 10, 10))
                        .email("quannguyenminh1001@gmail.com")
                        .password(passwordEncoder.encode("Cinema123123@"))
                        .gender(true)
                        .role(roleAdmin)
                        .status(UserStatus.ACTIVE)
                        .build());
            }
        }
        if (genreRepository.count() == 0 && actorRepository.count() == 0 && producerRepository.count() == 0 && directorRepository.count() == 0) {
            /* Insert genres */
            Genre horror = genreRepository.save(
                    Genre.builder()
                            .name("Kinh dị")
                            .status(BaseStatus.ACTIVE)
                            .build()
            );
            Genre mystery = genreRepository.save(
                    Genre.builder()
                            .name("Ly Kì")
                            .status(BaseStatus.ACTIVE)
                            .build()
            );
            Genre comedy = genreRepository.save(
                    Genre.builder()
                            .name("Hài")
                            .status(BaseStatus.ACTIVE)
                            .build()
            );
            Genre family = genreRepository.save(
                    Genre.builder()
                            .name("Gia Đình")
                            .status(BaseStatus.ACTIVE)
                            .build()
            );
            Genre action = genreRepository.save(
                    Genre.builder()
                            .name("Hành Động")
                            .status(BaseStatus.ACTIVE)
                            .build()
            );
            Genre romance = genreRepository.save(
                    Genre.builder()
                            .name("Tình Cảm")
                            .status(BaseStatus.ACTIVE)
                            .build()
            );
            Genre psychological = genreRepository.save(
                    Genre.builder()
                            .name("Tâm Lý")
                            .status(BaseStatus.ACTIVE)
                            .build()
            );
            Genre fantasy = genreRepository.save(
                    Genre.builder()
                            .name("Giả Tưởng")
                            .status(BaseStatus.ACTIVE)
                            .build()
            );

            /* Insert actors */
            Actor hoaiLinhActor = actorRepository.save(
                    Actor.builder()
                            .name("Hoài Linh")
                            .build()
            );

            Actor tuanTranActor = actorRepository.save(
                    Actor.builder()
                            .name("Tuấn Trần")
                            .build()
            );

            Actor leGiangActor = actorRepository.save(
                    Actor.builder()
                            .name("Lê Giang")
                            .build()
            );

            Actor diepBaoNgocActor = actorRepository.save(
                    Actor.builder()
                            .name("Diệp Bảo Ngọc")
                            .build()
            );

            Actor ryanReynoldsActor = actorRepository.save(
                    Actor.builder()
                            .name("Ryan Reynolds")
                            .build()
            );

            Actor hughJackmanActor = actorRepository.save(
                    Actor.builder()
                            .name("Hugh Jackman")
                            .build()
            );

            Actor patrickStewartActor = actorRepository.save(
                    Actor.builder()
                            .name("Patrick Stewart")
                            .build()
            );

            Actor trucAnhActor = actorRepository.save(
                    Actor.builder()
                            .name("Trúc Anh")
                            .build()
            );

            Actor tranNghiaActor = actorRepository.save(
                    Actor.builder()
                            .name("Trần Nghĩa")
                            .build()
            );

            Actor thaiHoaActor = actorRepository.save(
                    Actor.builder()
                            .name("Thái Hòa")
                            .build()
            );

            Actor thuTrangActor = actorRepository.save(
                    Actor.builder()
                            .name("Thu Trang")
                            .build()
            );

            Actor tienLuatActor = actorRepository.save(
                    Actor.builder()
                            .name("Tiến Luật")
                            .build()
            );

            Actor teeradetchMetawarayutActor = actorRepository.save(
                    Actor.builder()
                            .name("Teeradetch Metawarayut")
                            .build()
            );

            Actor chutavuthPattarakampolActor = actorRepository.save(
                    Actor.builder()
                            .name("Chutavuth Pattarakampol")
                            .build()
            );

            Actor johnnyFrenchActor = actorRepository.save(
                    Actor.builder()
                            .name("Johnny French")
                            .build()
            );

            Actor ranchraweeUakoolwarawatActor = actorRepository.save(
                    Actor.builder()
                            .name("Ranchrawee Uakoolwarawat")
                            .build()
            );

            Actor carolynBrackenActor = actorRepository.save(
                    Actor.builder()
                            .name("Carolyn Bracken")
                            .build()
            );

            Actor nicoleGarciaActor = actorRepository.save(
                    Actor.builder()
                            .name("Nicole Garcia")
                            .build()
            );

            Actor gerardDepardieuActor = actorRepository.save(
                    Actor.builder()
                            .name("Gérard Depardieu")
                            .build()
            );

            /* Insert directors */
            Director shawnLevyDirector = directorRepository.save(
                Director.builder()
                        .name("Shawn Levy")
                        .build()
            );

            Director trungLunDirector = directorRepository.save(
                    Director.builder()
                            .name("Trung Lùn")
                            .build()
            );

            Director victorVuDirector = directorRepository.save(
                    Director.builder()
                            .name("Victor Vũ")
                            .build()
            );

            Director jaturongMokjokDirector = directorRepository.save(
                    Director.builder()
                            .name("Jaturong Mokjok")
                            .build()
            );

            Director vuNgocDangDirector = directorRepository.save(
                    Director.builder()
                            .name("Vũ Ngọc Đãng")
                            .build()
            );

            Director damianMcCarthyDirector = directorRepository.save(
                    Director.builder()
                            .name("Damian Mc Carthy")
                            .build()
            );

            Director alainResnaisDirector = directorRepository.save(
                    Director.builder()
                            .name("Alain Resnais")
                            .build()
            );

            /* Insert producers */
            Producer centuryStudiosProducer = producerRepository.save(
                Producer.builder()
                        .name("20th Century Studios")
                        .build()
            );

            Producer marvelStudiosProducer = producerRepository.save(
                    Producer.builder()
                            .name("Marvel Studios")
                            .build()
            );

            Producer bluebellsStudiosProducer = producerRepository.save(
                    Producer.builder()
                            .name("Bluebells Studios")
                            .build()
            );

            Producer novemberFilmProducer = producerRepository.save(
                    Producer.builder()
                            .name("November Film")
                            .build()
            );

            Producer galaxyMEProducer = producerRepository.save(
                    Producer.builder()
                            .name("Galaxy M&E")
                            .build()
            );

            Producer thuTrangEntertainmentProducer = producerRepository.save(
                    Producer.builder()
                            .name("Thu Trang Entertainment")
                            .build()
            );

            Producer galaxyPlayProducer = producerRepository.save(
                    Producer.builder()
                            .name("Galaxy Play")
                            .build()
            );

            Producer galaxyStudioProducer = producerRepository.save(
                    Producer.builder()
                            .name("Galaxy Studio")
                            .build()
            );

            Producer velaEntertainmentProducer = producerRepository.save(
                    Producer.builder()
                            .name("Vela Entertainment")
                            .build()
            );

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
                                .slug("lam-giau-voi-ma")
                                .imagePortrait("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Flam-giau-voi-ma-2_1724686102964.jpg?alt=media")
                                .trailer("https://www.youtube.com/watch?v=2DmOv-pM1bM&t=2s")
                                .releaseDate(LocalDate.of(2024, 8, 29))
                                .status(MovieStatus.ACTIVE)
                                .genres(List.of(comedy, family))
                                .actors(List.of(hoaiLinhActor, tuanTranActor, leGiangActor, diepBaoNgocActor))
                                .directors(List.of(trungLunDirector))
                                .producers(List.of(bluebellsStudiosProducer))
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
                                .slug("deadpool--wolverine")
                                .imagePortrait("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fdeadpool--wolverine-500_1721640472363.jpg?alt=media")
                                .trailer("https://www.youtube.com/watch?v=2DmOv-pM1bM&t=2s")
                                .releaseDate(LocalDate.of(2024, 7, 27))
                                .status(MovieStatus.ACTIVE)
                                .genres(List.of(action, comedy, fantasy))
                                .actors(List.of(ryanReynoldsActor, hughJackmanActor, patrickStewartActor))
                                .directors(List.of(shawnLevyDirector))
                                .producers(List.of(centuryStudiosProducer, marvelStudiosProducer))
                                .build()
                );
                movieRepository.save(
                        Movie.builder()
                                .code("MV000000003")
                                .title("Mắt Biếc")
                                .duration(117)
                                .summary("")
                                .age(16)
                                .country("Việt Nam")
                                .rating(0)
                                .imageLandscape("")
                                .slug("mat-biec")
                                .imagePortrait("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2F300x450-mat-biec_1575021183171.jpg?alt=media")
                                .trailer("https://www.youtube.com/watch?v=KSFS0OfIK2c")
                                .releaseDate(LocalDate.of(2024, 9, 7))
                                .status(MovieStatus.ACTIVE)
                                .genres(List.of(romance))
                                .actors(List.of(trucAnhActor, tranNghiaActor))
                                .directors(List.of(victorVuDirector))
                                .producers(List.of(galaxyMEProducer, novemberFilmProducer))
                                .build()
                );
                movieRepository.save(
                        Movie.builder()
                                .code("MV000000004")
                                .title("Ông Chú Người Mỹ Của Tôi")
                                .duration(125)
                                .summary("")
                                .age(0)
                                .country("Pháp")
                                .rating(0)
                                .imageLandscape("")
                                .slug("my-american-uncle")
                                .imagePortrait("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fmy-american-uncle-1980-2_1725421699141.jpg?alt=medi")
                                .trailer("https://www.youtube.com/watch?v=bMYsSqV2npc")
                                .releaseDate(LocalDate.of(2024, 9, 7))
                                .status(MovieStatus.ACTIVE)
                                .genres(List.of(comedy, romance))
                                .actors(List.of(nicoleGarciaActor, gerardDepardieuActor))
                                .directors(List.of(alainResnaisDirector))
                                .build()
                );
                movieRepository.save(
                        Movie.builder()
                                .code("MV000000005")
                                .title("Xuyên Không Cải Mệnh Gia Tộc")
                                .duration(103)
                                .summary("")
                                .age(16)
                                .country("Thái Lan")
                                .rating(0)
                                .imageLandscape("")
                                .slug("chinatown-cha-cha")
                                .imagePortrait("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fchinatown-cha-cha-500_1724657094609.jpg?alt=media")
                                .trailer("https://www.youtube.com/watch?v=-oZaO57RAAc")
                                .releaseDate(LocalDate.of(2024, 9, 6))
                                .status(MovieStatus.ACTIVE)
                                .genres(List.of(comedy, action))
                                .actors(List.of(teeradetchMetawarayutActor, chutavuthPattarakampolActor, ranchraweeUakoolwarawatActor))
                                .directors(List.of(jaturongMokjokDirector))
                                .producers(List.of(velaEntertainmentProducer))
                                .build()
                );
                movieRepository.save(
                        Movie.builder()
                                .code("MV000000006")
                                .title("Con Nhót Mót Chồng")
                                .duration(112)
                                .summary("")
                                .age(16)
                                .country("Việt Nam")
                                .rating(0)
                                .imageLandscape("")
                                .slug("con-nhot-mot-chong")
                                .imagePortrait("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fcnmc-500_1704869791399.jpg?alt=media")
                                .trailer("https://www.youtube.com/watch?v=e7KHOQ-alqY")
                                .releaseDate(LocalDate.of(2024, 9, 4))
                                .status(MovieStatus.ACTIVE)
                                .genres(List.of(comedy, psychological))
                                .actors(List.of(thaiHoaActor, thuTrangActor, tienLuatActor))
                                .directors(List.of(vuNgocDangDirector))
                                .producers(List.of(galaxyPlayProducer, galaxyStudioProducer, thuTrangEntertainmentProducer))
                                .build()
                );
                movieRepository.save(
                        Movie.builder()
                                .code("MV000000007")
                                .title("Quỷ Án")
                                .duration(98)
                                .summary("")
                                .age(16)
                                .country("Ireland")
                                .rating(0)
                                .imageLandscape("")
                                .slug("oddity")
                                .imagePortrait("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Foddity-1_1725523860091.jpg?alt=media")
                                .trailer("https://www.youtube.com/watch?v=2DmOv-pM1bM&t=2s")
                                .releaseDate(LocalDate.of(2024, 9, 13))
                                .status(MovieStatus.ACTIVE)
                                .genres(List.of(horror, mystery))
                                .actors(List.of(johnnyFrenchActor, carolynBrackenActor))
                                .directors(List.of(damianMcCarthyDirector))
                                .build()
                );
            }
        }
    }
}

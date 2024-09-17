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
import java.time.LocalTime;
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
    private final CityRepository cityRepository;
    private final ShowTimeRepository showTimeRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        LocalDate currentDate = LocalDate.now();

        if (cityRepository.count() == 0) {
            City hcmCity = cityRepository.save(
                    City.builder()
                            .name("Hồ Chí Minh")
                            .build()
            );

            City haNoiCity = cityRepository.save(
                    City.builder()
                            .name("Hà Nội")
                            .build()
            );

            City benTreCity = cityRepository.save(
                    City.builder()
                            .name("Bến Tre")
                            .build()
            );

            City anGiangCity = cityRepository.save(
                    City.builder()
                            .name("An Giang")
                            .build()
            );

            City daklakCity = cityRepository.save(
                    City.builder()
                            .name("Đắk Lắk")
                            .build()
            );

            City haiPhongCity = cityRepository.save(
                    City.builder()
                            .name("Hải Phòng")
                            .build()
            );

            City ngheAnCity = cityRepository.save(
                    City.builder()
                            .name("Nghệ An")
                            .build()
            );
            City caMauCity = cityRepository.save(
                    City.builder()
                            .name("Cà Mau")
                            .build()
            );
            City daNangCity = cityRepository.save(
                    City.builder()
                            .name("Đà Nẵng")
                            .build()
            );

            if (cinemaRepository.count() == 0) {
                Cinema nguyenDuCinema = cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Nguyễn Du")
                                .address("116 Nguyễn Du")
                                .ward("Bến Thành")
                                .district("Quận 1")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-nguyen-du")
                                .build()
                );

                Room room1NguyenDuCinema = roomRepository.save(
                        Room.builder()
                                .name("Rạp 1")
                                .cinema(nguyenDuCinema)
                                .build()
                );

                Room room2NguyenDuCinema = roomRepository.save(
                        Room.builder()
                                .name("Rạp 2")
                                .cinema(nguyenDuCinema)
                                .build()
                );

                Room room3NguyenDuCinema = roomRepository.save(
                        Room.builder()
                                .name("Rạp 3")
                                .cinema(nguyenDuCinema)
                                .build()
                );

                Room room4NguyenDuCinema = roomRepository.save(
                        Room.builder()
                                .name("Rạp 2")
                                .cinema(nguyenDuCinema)
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Sala")
                                .address("Tầng 3, Thiso Mall Sala")
                                .ward("10 Mai Chí Thọ")
                                .district("Thủ Thiêm")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-sala")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Tân Bình")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-tan-binh")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Kinh Dương Vương")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-kinh-duong-vuong")
                                .build()
                );

                Cinema quangTrungCinema = cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Quang Trung")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-quang-trung")
                                .build()
                );

                Room room1QuangTrungCinema = roomRepository.save(
                        Room.builder()
                                .name("Rạp 1")
                                .cinema(quangTrungCinema)
                                .build()
                );

                Room room2QuangTrungCinema = roomRepository.save(
                        Room.builder()
                                .name("Rạp 2")
                                .cinema(quangTrungCinema)
                                .build()
                );

                Room room3QuangTrungCinema = roomRepository.save(
                        Room.builder()
                                .name("Rạp 3")
                                .cinema(quangTrungCinema)
                                .build()
                );

                Room room4QuangTrungCinema = roomRepository.save(
                        Room.builder()
                                .name("Rạp 2")
                                .cinema(quangTrungCinema)
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Trung Chánh")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-trung-chanh")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Huỳnh Tấn Phát")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-huynh-tan-phat")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Nguyễn Văn Quá")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-nguyen-van-qua")
                                .build()
                );
                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Co.opXtra Linh Trung")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-linh-trung")
                                .build()
                );
                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Trường Chinh")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-truong-trinh")
                                .build()
                );
                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Parc Mall Q8")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(hcmCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-parc-mall-q8")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy MIPEC Long Biên")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(haNoiCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-mipec-long-bien")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Tràng Thi")
                                .address("Nguyễn Kim Tràng Thi, 10B Tràng Thi")
                                .ward("Hàng Trống")
                                .district("Hoàn Kiếm")
                                .city(haNoiCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-trang-thi")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Bến Tre")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(benTreCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-ben-tre")
                                .build()
                );


                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Đà Nẵng")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(daNangCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-da-nang")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Cà Mau")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(caMauCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-ca-mau")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Vinh")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(ngheAnCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-vinh")
                                .build()
                );
                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Hải Phòng")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(haiPhongCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-hai-phong")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Buôn Ma Thuột")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(daklakCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-buon-ma-thuoc")
                                .build()
                );

                cinemaRepository.save(
                        Cinema.builder()
                                .name("Galaxy Long Xuyên")
                                .address("Tầng 3 - Co.opMart TTTM Thắng Lợi - Số 2 Trường Chinh")
                                .ward("Tây Thạnh")
                                .district("Tân Phú")
                                .city(anGiangCity)
                                .hotline("19002224")
                                .images(List.of("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media", "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"))
                                .slug("galaxy-long-xuyen")
                                .build()
                );

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

                    Actor vietHuongActor = actorRepository.save(
                            Actor.builder()
                                    .name("Việt Hương")
                                    .build()
                    );

                    Actor trungDanActor = actorRepository.save(
                            Actor.builder()
                                    .name("Trung Dân")
                                    .build()
                    );

                    Actor nsutThanhLocActor = actorRepository.save(
                            Actor.builder()
                                    .name("NSUT Thành Lộc")
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

                    Director nguyenHuuHoangDirector = directorRepository.save(
                            Director.builder()
                                    .name("Nguyễn Hữu Hoàng")
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
                        Movie lamGiauVoiMaMovie = movieRepository.save(
                                Movie.builder()
                                        .code("MV000000001")
                                        .title("Làm Giàu Với Ma")
                                        .duration(112)
                                        .summary("Phim mới Làm Giàu Với Ma kể về Lanh (Tuấn Trần) - con trai của ông Đạo làm nghề mai táng (Hoài Linh), lâm vào đường cùng vì cờ bạc. Trong cơn túng quẫn, “duyên tình” đẩy đưa anh gặp một ma nữ (Diệp Bảo Ngọc) và cùng nhau thực hiện những “kèo thơm\" để phục vụ mục đích của cả hai.")
                                        .age(16)
                                        .country("Việt Nam")
                                        .rating(0)
                                        .imageLandscape("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Flam-giau-voi-ma-3_1724686107530.jpg?alt=media")
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
                                        .summary("Deadpool 3 sẽ mang đến rất nhiều biến thể khác nhau của Wade Wilson, và không loại trừ khả năng một trong số họ đến từ dòng thời gian chính của MCU. Có không ít tin đồn liên quan đến việc Lady Deadpool cũng sẽ xuất hiện trong bom tấn này và do bà xã của Ryan Reynolds, Blake Lively thủ vai.")
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
                                        .summary("Đạo diễn Victor Vũ trở lại với một tác phẩm chuyển thể từ truyện ngắn cùng tên nổi tiếng của nhà văn Nguyễn Nhật Ánh: Mắt Biếc. Phim kể về chuyện tình đơn phương của chàng thanh niên Ngạn dành cho cô bạn từ thuở nhỏ Hà Lan... Ngạn và Hà Lan vốn là hai người bạn từ thuở nhỏ, cùng ở làng Đo Đo an bình. Họ cùng nhau đi học, cùng trải qua quãng đời áo trắng ngây thơ vụng dại với những cảm xúc bồi hồi của tuổi thiếu niên. \"Ngày cô ấy đi theo chốn phồn hoa, chàng trai bơ vơ từ xa...\", Hà Lan lên thành phố học và sớm bị thành thị xa hoa làm cho quên lãng Đo Đo. Cô quên mất cậu bạn thân mà chạy theo gã lãng tử hào hoa Dũng. Để rồi...")
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
                                        .summary("My American Uncle/ Ông Chú Người Mỹ Của Tôi là phim hài tình cảm thập niên 80. Jean Le Gall xuất thân từ giai cấp tư sản. Anh tham vọng theo đuổi sự nghiệp chính trị và văn học. Jean bỏ rơi vợ con mình chạy theo nữ diễn viên Janine Garnier. Janine rời xa gia đình để sống cuộc đời riêng. Theo yêu cầu của vợ Jean, Janine rời bỏ anh, sau đó trở thành cố vấn cho một tập đoàn dệt may. Tại đây, cô phải giải quyết vụ án hóc búa của René Ragueneau – từ  con trai nông dântrở thành giám đốc nhà máy. Giáo sư Henri Laborit quan sát câu chuyện của họ, thông qua đó, giải thích về hành vi của con người.")
                                        .age(0)
                                        .country("Pháp")
                                        .rating(0)
                                        .imageLandscape("")
                                        .slug("my-american-uncle")
                                        .imagePortrait("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fmy-american-uncle-1980-2_1725421699141.jpg?alt=media")
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
                                        .summary("Xuyên Không Cải Mệnh Gia Tộc xoay quanh Kie (Mint Ranchrawee), một sinh viên ngành khảo cổ học, sinh ra trong một gia đình gốc Hoa tại khu phố Tàu. Ngay từ nhỏ, Kie đã chứng kiến gia đình liên tục dính xui xẻo, ngồi không cũng gặp chuyện. Trong một lần cầu nguyện giải xui tại ngôi đền trong xóm, cô được thầy cúng tại đây cho biết, vận xui gia đình cô là nghiệp chướng do Kung (Alek Teeradetch) - ông tổ nhà cô gây ra. Dù không tin lắm vào trò mê tín, Kie nhờ thầy cúng giúp mình thực hiện nghi lễ thanh tẩy nghiệp chướng, cô vô tình bị đẩy về quá khứ và nhập xác vào người anh em thân thiết nhất của ông cố mình - Tai do March Chutavuth thủ vai. Từ đây, hàng loạt tình huống giở khóc giở cười liên tục ập đến, đồng thời những bí mật đen tối từ quá khứ đã dần được hé lộ.")
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
                                        .summary("Lấy cảm hứng từ web drama Chuyện Xóm Tui, phiên bản điện ảnh sẽ mang một màu sắc hoàn toàn khác: hài hước hơn, gần gũi và nhiều cảm xúc hơn. Bộ phim là câu chuyện của Nhót - người phụ nữ “chưa kịp già” đã sắp bị mãn kinh, vội vàng đi tìm chồng. Nhưng sâu thẳm trong cô là khao khát muốn có một đứa con, và luôn muốn hàn gắn với người cha suốt ngày say xỉn của mình.")
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
                                        .summary("Oddity/ Quỷ Án kể về vụ án người phụ nữ Dani bị sát hại dã man tại ngôi nhà mà vợ chồng cô đang sửa sang ở vùng nông thôn hẻo lánh. Chồng cô - Ted đang làm bác sĩ tại bệnh viện tâm thần. Mọi nghi ngờ đổ dồn vào một bệnh nhân tại đây. Không may, nghi phạm đã chết. Một năm sau, em gái mù của Dani ghé tới. Darcy là nhà ngoại cảm tự xưng, mang theo nhiều món đồ kì quái. Cô đến nhà Ted để tìm chân tướng về cái chết của chị gái.")
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

                        movieRepository.save(
                                Movie.builder()
                                        .code("MV000000008")
                                        .title("Ma Da")
                                        .duration(94)
                                        .summary("Phim kể về hành trình của bà Lệ, người làm nghề vớt xác người chết trên sông để đưa về với gia đình. Trong quá trình làm nghề, bà Lệ đắc tội với Ma Da, một oan hồn sống dưới sông nước thường xuyên kéo chân người để thế mạng cho mình đi đầu thai. Ân oán của cả hai khiến cho Ma Da bắt mất bé Nhung, con gái của bà Lệ. Bà Lệ phải nhờ đến sự giúp đỡ của những người bên cạnh để cùng nhau lên đường tìm cách cứu bé Nhung và mở ra những bí mật đằng sau oan hồn Ma Da kia.")
                                        .age(16)
                                        .country("Việt Nam")
                                        .rating(0)
                                        .imageLandscape("")
                                        .slug("ma-da")
                                        .imagePortrait("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fma-da-500_1723799717930.jpg?alt=media")
                                        .trailer("https://www.youtube.com/watch?v=l0E3vhaG9i4&t=1s")
                                        .releaseDate(LocalDate.of(2024, 8, 15))
                                        .status(MovieStatus.ACTIVE)
                                        .genres(List.of(horror))
                                        .actors(List.of(vietHuongActor, trungDanActor, nsutThanhLocActor))
                                        .directors(List.of(nguyenHuuHoangDirector))
                                        .build()
                        );



                        if (showTimeRepository.count() == 0) {
                            showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room2QuangTrungCinema)
                                        .startDate(currentDate)
                                        .startTime(LocalTime.of(9, 30))
                                        .endTime(LocalTime.of(11, 30))
                                        .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(quangTrungCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room2QuangTrungCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(10, 30))
                                            .endTime(LocalTime.of(12, 30))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(quangTrungCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room1QuangTrungCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(11, 45))
                                            .endTime(LocalTime.of(13, 45))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(quangTrungCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room3QuangTrungCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(13, 0))
                                            .endTime(LocalTime.of(15, 0))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(quangTrungCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room4QuangTrungCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(14, 0))
                                            .endTime(LocalTime.of(16, 0))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(quangTrungCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room1QuangTrungCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(16, 0))
                                            .endTime(LocalTime.of(18, 0))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(quangTrungCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room2QuangTrungCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(18, 0))
                                            .endTime(LocalTime.of(20, 0))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(quangTrungCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room3QuangTrungCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(20, 0))
                                            .endTime(LocalTime.of(22, 0))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(quangTrungCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room4QuangTrungCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(21, 15))
                                            .endTime(LocalTime.of(23, 15))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(quangTrungCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room3QuangTrungCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(22, 15))
                                            .endTime(LocalTime.of(0, 15))
                                            .build()
                            );

                            // movie in Nguyen Du Cinema
                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(nguyenDuCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room2NguyenDuCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(19, 0))
                                            .endTime(LocalTime.of(21, 0))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(nguyenDuCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room1NguyenDuCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(20, 0))
                                            .endTime(LocalTime.of(22, 0))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(nguyenDuCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room3NguyenDuCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(21, 0))
                                            .endTime(LocalTime.of(23, 0))
                                            .build()
                            );

                            showTimeRepository.save(
                                    ShowTime.builder()
                                            .cinema(nguyenDuCinema)
                                            .movie(lamGiauVoiMaMovie)
                                            .room(room4NguyenDuCinema)
                                            .startDate(currentDate)
                                            .startTime(LocalTime.of(22, 15))
                                            .endTime(LocalTime.of(0, 15))
                                            .build()
                            );
                        }
                    }
                }
            }
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
    }
}

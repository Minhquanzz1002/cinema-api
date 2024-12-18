package vn.edu.iuh.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.edu.iuh.models.*;
import vn.edu.iuh.models.enums.*;
import vn.edu.iuh.repositories.*;
import vn.edu.iuh.services.impl.CinemaServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static vn.edu.iuh.constant.SecurityConstant.ROLE_CLIENT;

@Slf4j
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
    private final ShowTimeRepository showTimeRepository;
    private final RoomRepository roomRepository;
    private final RoomLayoutRepository roomLayoutRepository;
    private final RowSeatRepository rowSeatRepository;
    private final SeatRepository seatRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final GroupSeatRepository groupSeatRepository;
    private final TicketPriceRepository ticketPriceRepository;
    private final TicketPriceLineRepository ticketPriceLineRepository;
    private final TicketPriceDetailRepository ticketPriceDetailRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionLineRepository promotionLineRepository;
    private final PromotionDetailRepository promotionDetailRepository;
    private final RefundRepository refundRepository;
    private final PasswordEncoder passwordEncoder;
    private final CinemaServiceImpl cinemaService;

    private final LocalDate currentDate = LocalDate.now();
    private final LocalDate tomorrowDate = LocalDate.now().plusDays(1);
    private final LocalDate todayPlus2Days = LocalDate.now().plusDays(2);
    private ShowTime lamGiauVoiMaShowTime;
    private ShowTime deadpoolShowTime;
    private ShowTime deadpoolShowTime_NguyenDuCinema;
    private ShowTime maDaShowTime;

    private ShowTime quyAnShowTime;

    private ShowTime matBiecShowTime;

    private ShowTime ongChuNguoiMyCuaToiShowTime;

    private ShowTime xuyenKhongCaiManhGiaTocShowTime;

    private ShowTime conNhotMotChongShowTime_SalaCinema;

    private Product product;
    
    private static final List<String> DEFAULT_CINEMA_IMAGES = List.of(
            "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F1_1703500551418.jpg?alt=media",
            "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F2_1703500555704.jpg?alt=media",
            "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F3_1703500560520.jpg?alt=media",
            "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F4_1703500565605.jpg?alt=media",
            "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/cinemas%2F5_1703500570351.jpg?alt=media"
    );

    private static final String DEFAULT_PASSWORD = "Cinema123123@";

    @Override
    public void run(String... args) {
        if (cinemaRepository.count() == 0) {
            int[][][] layout3 = {
                    {{14}, {13}, {12}, {11}, {10}, {9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{14}, {13}, {12}, {11}, {10}, {9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{14}, {13}, {12}, {11}, {10}, {9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{14}, {13}, {12}, {11}, {10}, {9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{14}, {13}, {12}, {11}, {10}, {9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{14}, {13}, {12}, {11}, {10}, {9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{14}, {13}, {12}, {11}, {10}, {9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{14}, {13}, {12}, {11}, {10}, {9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
            };

            int[][][] layout2 = {
                    {{8}, {7}, {0}, {6, 5}, {5, 6}, {0}, {4}, {3}, {0}, {2, 1}, {1, 2}},
                    {},
                    {{7, 6}, {6, 7}, {0}, {5}, {0}, {0}, {4, 3}, {3, 4}, {0}, {2}, {1}},
                    {},
                    {{7}, {0}, {6, 5}, {5, 6}, {0}, {0}, {4}, {3}, {0}, {2, 1}, {1, 2}},
                    {},
                    {{7, 6}, {6, 7}, {0}, {5}, {0}, {0}, {4, 3}, {3, 4}, {0}, {2}, {1}}
            };

            int[][][] layout1 = {
                    {{0}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{0}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{0}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{0}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
                    {{9}, {8}, {7}, {6}, {5}, {4}, {3}, {2}, {1}},
            };

            int[][][] layout4 = {
                    {{0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {11}, {12}, {13}, {14}},
                    {{1}, {2}, {0}, {0}, {0}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {11}, {12}},
                    {{1}, {2}, {0}, {0}, {0}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {11}, {12}},
                    {{1}, {2}, {0}, {0}, {0}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {0}, {0}},
                    {{1}, {2}, {0}, {0}, {0}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {0}, {0}},
                    {{1}, {2}, {0}, {0}, {0}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {0}, {0}},
                    {{1}, {2}, {0}, {0}, {0}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {0}, {0}},
                    {{1}, {2}, {0}, {0}, {0}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {0}, {0}},
                    {{1}, {2}, {0}, {0}, {0}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {0}, {0}},
                    {{1}, {2}, {0}, {0}, {0}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10}, {0}, {0}},
            };

            Cinema nguyenDuCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Nguyễn Du")
                          .address("116 Nguyễn Du")
                          .ward("Phường Bến Thành")
                          .wardCode("26743")
                          .district("Quận 1")
                          .districtCode("760")
                          .city("Thành phố Hồ Chí Minh")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-nguyen-du")
                          .build()
            );

            Room room1NguyenDuCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 1")
                        .cinema(nguyenDuCinema)
                        .build()
            );

            insertLayout(layout4, room1NguyenDuCinema, 59);

            Room room2NguyenDuCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 2")
                        .cinema(nguyenDuCinema)
                        .build()
            );

            insertLayout(layout3, room2NguyenDuCinema, 112);

            Room room3NguyenDuCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 3")
                        .cinema(nguyenDuCinema)
                        .build()
            );

            insertLayout(layout2, room3NguyenDuCinema, 21);

            Room room4NguyenDuCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 2")
                        .cinema(nguyenDuCinema)
                        .build()
            );

            insertLayout(layout1, room4NguyenDuCinema, 77);

            Cinema salaCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Sala")
                          .address("Tầng 3, Thiso Mall Sala")
                          .ward("Phường Thủ Thiêm")
                          .wardCode("27118")
                          .district("Quận 2")
                          .districtCode("769")
                          .city("Thành phố Hồ Chí Minh")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-sala")
                          .build()
            );

            Room room1SalaCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 1")
                        .cinema(salaCinema)
                        .build()
            );

            insertLayout(layout4, room1SalaCinema, 59);

            Room room2SalaCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 2")
                        .cinema(salaCinema)
                        .build()
            );

            insertLayout(layout3, room2SalaCinema, 112);

            Room room3SalaCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 3")
                        .cinema(salaCinema)
                        .build()
            );

            insertLayout(layout2, room3SalaCinema, 21);

            Room room4SalaCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 2")
                        .cinema(salaCinema)
                        .build()
            );

            insertLayout(layout1, room4SalaCinema, 77);

            Cinema tanBinhCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Tân Bình")
                          .address("246 Đ. Nguyễn Hồng Đào")
                          .ward("Phường 14")
                          .wardCode("27004")
                          .district("Quận Tân Bình")
                          .districtCode("766")
                          .city("Thành phố Hồ Chí Minh")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-tan-binh")
                          .build()
            );

            insertRooms(tanBinhCinema, layout1, layout2, layout3);

            Cinema kinhDuongVuongCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Kinh Dương Vương")
                          .address("718bis Đ. Kinh Dương Vương")
                          .ward("Phường 13")
                          .wardCode("27349")
                          .district("Quận 6")
                          .districtCode("775")
                          .city("Thành phố Hồ Chí Minh")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-kinh-duong-vuong")
                          .build()
            );

            insertRooms(kinhDuongVuongCinema, layout1, layout2, layout3);

            Cinema quangTrungCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Quang Trung")
                          .address("Lầu 3, TTTM CoopMart Foodcosa số 304A, Quang Trung")
                          .ward("Phường 11")
                          .wardCode("26899")
                          .district("Quận Gò Vấp")
                          .districtCode("764")
                          .city("Thành phố Hồ Chí Minh")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-quang-trung")
                          .build()
            );

            Room room1QuangTrungCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 1")
                        .cinema(quangTrungCinema)
                        .build()
            );

            insertLayout(layout1, room1QuangTrungCinema, 77);

            Room room2QuangTrungCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 2")
                        .cinema(quangTrungCinema)
                        .build()
            );

            insertLayout(layout2, room2QuangTrungCinema, 21);

            Room room3QuangTrungCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 3")
                        .cinema(quangTrungCinema)
                        .build()
            );

            insertLayout(layout3, room3QuangTrungCinema, 112);

            Room room4QuangTrungCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 4")
                        .cinema(quangTrungCinema)
                        .build()
            );

            insertLayout(layout1, room4QuangTrungCinema, 77);

            Cinema huynhTanPhatCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Huỳnh Tấn Phát")
                          .address("1362 Huỳnh Tấn Phát")
                          .ward("Phường Phú Mỹ")
                          .wardCode("27493")
                          .district("Quận 7")
                          .districtCode("778")
                          .city("Thành phố Hồ Chí Minh")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-huynh-tan-phat")
                          .build()
            );
            insertRooms(huynhTanPhatCinema, layout1, layout2, layout3);

            Cinema nguyenVanQuaCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Nguyễn Văn Quá")
                          .address("119B Đ. Nguyễn Văn Quá")
                          .ward("Phường Đông Hưng Thuận")
                          .wardCode("26788")
                          .district("Quận 12")
                          .districtCode("761")
                          .city("Thành phố Hồ Chí Minh")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-nguyen-van-qua")
                          .build()
            );
            insertRooms(nguyenVanQuaCinema, layout1, layout2, layout3);

            Cinema linhTrungCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Co.opXtra Linh Trung")
                          .address("934 QL1A")
                          .ward("Phường Linh Trung")
                          .wardCode("26800")
                          .district("Quận Thủ Đức")
                          .districtCode("762")
                          .city("Thành phố Hồ Chí Minh")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-linh-trung")
                          .build()
            );
            insertRooms(linhTrungCinema, layout1, layout2, layout3);

            Cinema truongTrinhCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Trường Chinh")
                          .address("Co.opMart TTTM Thắng Lợi, 2 Đ. Trường Chinh")
                          .ward("Phường Tây Thạnh")
                          .wardCode("27013")
                          .district("Quận Tân Phú")
                          .districtCode("767")
                          .city("Thành phố Hồ Chí Minh")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-truong-trinh")
                          .build()
            );
            insertRooms(truongTrinhCinema, layout1, layout2, layout3);

            Cinema mallParcCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Parc Mall Q8")
                          .address("549 Đ. Tạ Quang Bửu")
                          .ward("Phường 04")
                          .wardCode("27409")
                          .district("Quận 8")
                          .districtCode("776")
                          .city("Thành phố Hồ Chí Minh")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-parc-mall-q8")
                          .build()
            );
            insertRooms(mallParcCinema, layout1, layout2, layout3);

            Cinema longBienCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q MIPEC Long Biên")
                          .address("Chung Cư Mipec Riverside")
                          .ward("Phường Ngọc Lâm")
                          .wardCode("00133")
                          .district("Quận Long Biên")
                          .districtCode("004")
                          .city("Thành phố Hà Nội")
                          .cityCode("01")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-mipec-long-bien")
                          .build()
            );
            insertRooms(longBienCinema, layout1, layout2, layout3);

            Cinema trangThiCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Tràng Thi")
                          .address("10b P. Tràng Thi")
                          .ward("Phường Hàng Trống")
                          .wardCode("00070")
                          .district("Quận Hoàn Kiếm")
                          .districtCode("002")
                          .city("Thành phố Hà Nội")
                          .cityCode("01")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-trang-thi")
                          .build()
            );
            insertRooms(trangThiCinema, layout1, layout2, layout3);

            Cinema benTreCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Bến Tre")
                          .address("Lầu 1, TTTM Sense City, 26A Trần Quốc Tuấn")
                          .ward("Phường 4")
                          .wardCode("28765")
                          .district("Thành phố Bến Tre")
                          .districtCode("829")
                          .city("Tỉnh Bến Tre")
                          .cityCode("83")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-ben-tre")
                          .build()
            );
            insertRooms(benTreCinema, layout1, layout2, layout3);


            Cinema daNangCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Đà Nẵng")
                          .address("Coop Mart, 478 Điện Biên Phủ")
                          .ward("Phường Thanh Khê Đông")
                          .wardCode("20207")
                          .district("Quận Thanh Khê")
                          .districtCode("491")
                          .city("Thành phố Đà Nẵng")
                          .cityCode("48")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-da-nang")
                          .build()
            );
            insertRooms(daNangCinema, layout1, layout2, layout3);

            Cinema caMauCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Cà Mau")
                          .address("9 Đ. Trần Hưng Đạo")
                          .ward("Phường 5")
                          .wardCode("32008")
                          .district("Thành phố Cà Mau")
                          .districtCode("964")
                          .city("Tỉnh Cà Mau")
                          .cityCode("96")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-ca-mau")
                          .build()
            );
            insertRooms(caMauCinema, layout1, layout2, layout3);

            Cinema vinhCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Vinh")
                          .address("1 Đ. Lê Hồng Phong")
                          .ward("Phường Hưng Bình")
                          .wardCode("16672")
                          .district("Thành phố Vinh")
                          .districtCode("412")
                          .city("Tỉnh Nghệ An")
                          .cityCode("40")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-vinh")
                          .build()
            );
            insertRooms(vinhCinema, layout1, layout2, layout3);

            Cinema haiPhongCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Hải Phòng")
                          .address("104 P. Lương Khánh Thiện")
                          .ward("Phường Lương Khánh Thiện")
                          .wardCode("11344")
                          .district("Quận Ngô Quyền")
                          .districtCode("304")
                          .city("Thành phố Hải Phòng")
                          .cityCode("31")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-hai-phong")
                          .build()
            );

            insertRooms(haiPhongCinema, layout1, layout2, layout3);

            Cinema buonMaThuotCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Buôn Ma Thuột")
                          .address("71 Nguyễn Tất Thành")
                          .ward("Phường Tân An")
                          .wardCode("24124")
                          .district("Thành phố Buôn Ma Thuột")
                          .districtCode("643")
                          .city("Tỉnh Đắk Lắk")
                          .cityCode("66")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-buon-ma-thuoc")
                          .build()
            );

            Room room1BuonMaThuotCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 1")
                        .cinema(buonMaThuotCinema)
                        .build()
            );

            insertLayout(layout1, room1BuonMaThuotCinema, 77);

            Room room2BuonMaThuotCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 2")
                        .cinema(buonMaThuotCinema)
                        .build()
            );

            insertLayout(layout2, room2BuonMaThuotCinema, 21);

            Room room3BuonMaThuotCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 3")
                        .cinema(buonMaThuotCinema)
                        .build()
            );

            insertLayout(layout3, room3BuonMaThuotCinema, 112);

            Room room4BuonMaThuotCinema = roomRepository.save(
                    Room.builder()
                        .name("Phòng 4")
                        .cinema(buonMaThuotCinema)
                        .build()
            );

            insertLayout(layout1, room4BuonMaThuotCinema, 77);

            Cinema longXuyenCinema = cinemaRepository.save(
                    Cinema.builder()
                          .code(cinemaService.generateCinemaCode())
                          .name("B&Q Long Xuyên")
                          .address("Nguyễn Kim/01 Trần Hưng Đạo")
                          .ward("Phường Mỹ Bình")
                          .wardCode("30280")
                          .district("Thành phố Long Xuyên")
                          .districtCode("883")
                          .city("Tỉnh An Giang")
                          .cityCode("79")
                          .hotline("19002224")
                          .images(DEFAULT_CINEMA_IMAGES)
                          .slug("galaxy-long-xuyen")
                          .build()
            );

            insertRooms(longXuyenCinema, layout1, layout2, layout3);

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
                             .code("DV000001")
                             .name("Hoài Linh")
                             .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/actors%2FHoai-Linh.jpg?alt=media")
                             .country("Việt Nam")
                             .build()
                );

                Actor tuanTranActor = actorRepository.save(
                        Actor.builder()
                             .name("Tuấn Trần")
                             .code("DV000002")
                             .country("Việt Nam")
                             .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/actors%2Ftuan-tran.webp?alt=media")
                             .build()
                );

                Actor leGiangActor = actorRepository.save(
                        Actor.builder()
                             .name("Lê Giang")
                             .code("DV000003")
                             .country("Việt Nam")
                             .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/actors%2Fle-giang.webp?alt=media")
                             .build()
                );

                Actor diepBaoNgocActor = actorRepository.save(
                        Actor.builder()
                             .name("Diệp Bảo Ngọc")
                             .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/actors%2Fdbn-tren-phim-truong-4-2098.jpg?alt=media")
                             .code("DV000004")
                             .country("Việt Nam")
                             .build()
                );

                Actor ryanReynoldsActor = actorRepository.save(
                        Actor.builder()
                             .name("Ryan Reynolds")
                             .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/actors%2FRyan%20Reynolds.jpg?alt=media")
                             .code("DV000005")
                             .build()
                );

                Actor hughJackmanActor = actorRepository.save(
                        Actor.builder()
                             .name("Hugh Jackman")
                             .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/actors%2FHugh%20Jackman.jpg?alt=media")
                             .code("DV000006")
                             .build()
                );

                Actor patrickStewartActor = actorRepository.save(
                        Actor.builder()
                             .name("Patrick Stewart")
                             .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/actors%2FPatrick%20Stewart.jpg?alt=media")
                             .code("DV000007")
                             .build()
                );

                Actor trucAnhActor = actorRepository.save(
                        Actor.builder()
                             .name("Trúc Anh")
                             .code("DV000008")
                             .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/actors%2FTrucAnh.webp?alt=media")
                             .country("Việt Nam")
                             .build()
                );

                Actor tranNghiaActor = actorRepository.save(
                        Actor.builder()
                             .name("Trần Nghĩa")
                             .code("DV000009")
                             .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/actors%2Ftrannghia.jpg?alt=media")
                             .country("Việt Nam")
                             .build()
                );

                Actor thaiHoaActor = actorRepository.save(
                        Actor.builder()
                             .name("Thái Hòa")
                             .code("DV000010")
                             .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/actors%2Fdien-vien-thai-hoa.jpg?alt=media")
                             .country("Việt Nam")
                             .build()
                );

                Actor thuTrangActor = actorRepository.save(
                        Actor.builder()
                             .name("Thu Trang")
                             .code("DV000011")
                             .country("Việt Nam")
                             .build()
                );

                Actor tienLuatActor = actorRepository.save(
                        Actor.builder()
                             .name("Tiến Luật")
                             .code("DV000012")
                             .country("Việt Nam")
                             .build()
                );

                Actor teeradetchMetawarayutActor = actorRepository.save(
                        Actor.builder()
                             .code("DV000013")
                             .name("Teeradetch Metawarayut")
                             .build()
                );

                Actor chutavuthPattarakampolActor = actorRepository.save(
                        Actor.builder()
                             .code("DV000014")
                             .name("Chutavuth Pattarakampol")
                             .build()
                );

                Actor johnnyFrenchActor = actorRepository.save(
                        Actor.builder()
                             .name("Johnny French")
                             .code("DV000015")
                             .build()
                );

                Actor ranchraweeUakoolwarawatActor = actorRepository.save(
                        Actor.builder()
                             .code("DV000016")
                             .name("Ranchrawee Uakoolwarawat")
                             .build()
                );

                Actor carolynBrackenActor = actorRepository.save(
                        Actor.builder()
                             .code("DV000017")
                             .name("Carolyn Bracken")
                             .build()
                );

                Actor nicoleGarciaActor = actorRepository.save(
                        Actor.builder()
                             .code("DV000018")
                             .name("Nicole Garcia")
                             .build()
                );

                Actor gerardDepardieuActor = actorRepository.save(
                        Actor.builder()
                             .code("DV000019")
                             .name("Gérard Depardieu")
                             .build()
                );

                Actor vietHuongActor = actorRepository.save(
                        Actor.builder()
                             .name("Việt Hương")
                             .code("DV000020")
                             .country("Việt Nam")
                             .build()
                );

                Actor trungDanActor = actorRepository.save(
                        Actor.builder()
                             .name("Trung Dân")
                             .code("DV000021")
                             .country("Việt Nam")
                             .build()
                );

                Actor nsutThanhLocActor = actorRepository.save(
                        Actor.builder()
                             .name("NSUT Thành Lộc")
                             .code("DV000022")
                             .country("Việt Nam")
                             .build()
                );

                /* Insert directors */
                Director shawnLevyDirector = directorRepository.save(
                        Director.builder()
                                .code("DD000001")
                                .name("Shawn Levy")
                                .birthday(LocalDate.of(1968, 7, 23))
                                .bio("Shawn Adam Levy, thường được biết đến với tên gọi Shawn Levy, là một nam nhà làm phim kiêm diễn viên người Canada gốc Do Thái.")
                                .country("Canada")
                                .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/directors%2FShawn%20Levy.jpg?alt=media")
                                .build()
                );

                Director trungLunDirector = directorRepository.save(
                        Director.builder()
                                .code("DD000002")
                                .name("Trung Lùn")
                                .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/directors%2Ftrung-lun-1726469956247139623202.webp?alt=media")
                                .build()
                );

                Director victorVuDirector = directorRepository.save(
                        Director.builder()
                                .code("DD000003")
                                .name("Victor Vũ")
                                .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/directors%2Fvictorvu1-1637755775-7758-1637757291.jpg?alt=media")
                                .build()
                );

                Director jaturongMokjokDirector = directorRepository.save(
                        Director.builder()
                                .code("DD000004")
                                .name("Jaturong Mokjok")
                                .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/directors%2FJaturong%20Mokjok.jpg?alt=media")
                                .build()
                );

                Director vuNgocDangDirector = directorRepository.save(
                        Director.builder()
                                .code("DD000005")
                                .name("Vũ Ngọc Đãng")
                                .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/directors%2Fvungocdang.jpg?alt=media")
                                .build()
                );

                Director damianMcCarthyDirector = directorRepository.save(
                        Director.builder()
                                .code("DD000006")
                                .name("Damian Mc Carthy")
                                .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/directors%2FDamian%20Mc%20Carthy.jpg?alt=media")
                                .build()
                );

                Director alainResnaisDirector = directorRepository.save(
                        Director.builder()
                                .code("DD000007")
                                .name("Alain Resnais")
                                .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/directors%2FAlain%20Resnais.jpg?alt=media")
                                .build()
                );

                Director nguyenHuuHoangDirector = directorRepository.save(
                        Director.builder()
                                .code("DD000008")
                                .name("Nguyễn Hữu Hoàng")
                                .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/directors%2Fthumb_660_94420a8b-7168-4e1f-b0f4-3ce93e890c1b.jpg?alt=media")
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
                                 .code("MV000001")
                                 .title("Làm Giàu Với Ma")
                                 .duration(112)
                                 .summary(
                                         "Phim mới Làm Giàu Với Ma kể về Lanh (Tuấn Trần) - con trai của ông Đạo làm nghề mai táng (Hoài Linh), lâm vào đường cùng vì cờ bạc. Trong cơn túng quẫn, “duyên tình” đẩy đưa anh gặp một ma nữ (Diệp Bảo Ngọc) và cùng nhau thực hiện những “kèo thơm\" để phục vụ mục đích của cả hai.")
                                 .country("Việt Nam")
                                 .rating(0)
                                 .imageLandscape(
                                         "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Flam-giau-voi-ma-3_1724686107530.jpg?alt=media")
                                 .slug("lam-giau-voi-ma")
                                 .imagePortrait(
                                         "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Flam-giau-voi-ma-2_1724686102964.jpg?alt=media")
                                 .trailer("https://www.youtube.com/watch?v=2DmOv-pM1bM&t=2s")
                                 .releaseDate(LocalDate.of(2024, 8, 29))
                                 .status(MovieStatus.ACTIVE)
                                 .genres(List.of(comedy, family))
                                 .actors(List.of(hoaiLinhActor, tuanTranActor, leGiangActor, diepBaoNgocActor))
                                 .directors(List.of(trungLunDirector))
                                 .producers(List.of(bluebellsStudiosProducer))
                                 .build()
                    );
                    Movie deadpoolMovie = movieRepository.save(
                            Movie.builder()
                                 .code("MV000002")
                                 .title("Deadpool & Wolverine")
                                 .duration(127)
                                 .summary(
                                         "Deadpool 3 sẽ mang đến rất nhiều biến thể khác nhau của Wade Wilson, và không loại trừ khả năng một trong số họ đến từ dòng thời gian chính của MCU. Có không ít tin đồn liên quan đến việc Lady Deadpool cũng sẽ xuất hiện trong bom tấn này và do bà xã của Ryan Reynolds, Blake Lively thủ vai.")
                                 .ageRating(AgeRating.T18)
                                 .country("Mỹ")
                                 .rating(0)
                                 .imageLandscape("")
                                 .slug("deadpool--wolverine")
                                 .imagePortrait(
                                         "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fdeadpool--wolverine-500_1721640472363.jpg?alt=media")
                                 .trailer("https://www.youtube.com/watch?v=2DmOv-pM1bM&t=2s")
                                 .releaseDate(LocalDate.of(2024, 7, 27))
                                 .status(MovieStatus.ACTIVE)
                                 .genres(List.of(action, comedy, fantasy))
                                 .actors(List.of(ryanReynoldsActor, hughJackmanActor, patrickStewartActor))
                                 .directors(List.of(shawnLevyDirector))
                                 .producers(List.of(centuryStudiosProducer, marvelStudiosProducer))
                                 .build()
                    );
                    Movie matBiecMovie = movieRepository.save(
                            Movie.builder()
                                 .code("MV000003")
                                 .title("Mắt Biếc")
                                 .duration(117)
                                 .summary(
                                         "Đạo diễn Victor Vũ trở lại với một tác phẩm chuyển thể từ truyện ngắn cùng tên nổi tiếng của nhà văn Nguyễn Nhật Ánh: Mắt Biếc. Phim kể về chuyện tình đơn phương của chàng thanh niên Ngạn dành cho cô bạn từ thuở nhỏ Hà Lan... Ngạn và Hà Lan vốn là hai người bạn từ thuở nhỏ, cùng ở làng Đo Đo an bình. Họ cùng nhau đi học, cùng trải qua quãng đời áo trắng ngây thơ vụng dại với những cảm xúc bồi hồi của tuổi thiếu niên. \"Ngày cô ấy đi theo chốn phồn hoa, chàng trai bơ vơ từ xa...\", Hà Lan lên thành phố học và sớm bị thành thị xa hoa làm cho quên lãng Đo Đo. Cô quên mất cậu bạn thân mà chạy theo gã lãng tử hào hoa Dũng. Để rồi...")
                                 .country("Việt Nam")
                                 .rating(0)
                                 .imageLandscape("")
                                 .slug("mat-biec")
                                 .imagePortrait(
                                         "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2F300x450-mat-biec_1575021183171.jpg?alt=media")
                                 .trailer("https://www.youtube.com/watch?v=KSFS0OfIK2c")
                                 .releaseDate(LocalDate.of(2024, 9, 7))
                                 .status(MovieStatus.ACTIVE)
                                 .genres(List.of(romance))
                                 .actors(List.of(trucAnhActor, tranNghiaActor))
                                 .directors(List.of(victorVuDirector))
                                 .producers(List.of(galaxyMEProducer, novemberFilmProducer))
                                 .build()
                    );

                    Movie ongChuNguoiMyCuaToiMovie = movieRepository.save(
                            Movie.builder()
                                 .code("MV000004")
                                 .title("Ông Chú Người Mỹ Của Tôi")
                                 .duration(125)
                                 .summary(
                                         "My American Uncle/ Ông Chú Người Mỹ Của Tôi là phim hài tình cảm thập niên 80. Jean Le Gall xuất thân từ giai cấp tư sản. Anh tham vọng theo đuổi sự nghiệp chính trị và văn học. Jean bỏ rơi vợ con mình chạy theo nữ diễn viên Janine Garnier. Janine rời xa gia đình để sống cuộc đời riêng. Theo yêu cầu của vợ Jean, Janine rời bỏ anh, sau đó trở thành cố vấn cho một tập đoàn dệt may. Tại đây, cô phải giải quyết vụ án hóc búa của René Ragueneau – từ  con trai nông dântrở thành giám đốc nhà máy. Giáo sư Henri Laborit quan sát câu chuyện của họ, thông qua đó, giải thích về hành vi của con người.")
                                 .country("Pháp")
                                 .rating(0)
                                 .imageLandscape("")
                                 .slug("my-american-uncle")
                                 .imagePortrait(
                                         "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fmy-american-uncle-1980-2_1725421699141.jpg?alt=media")
                                 .trailer("https://www.youtube.com/watch?v=bMYsSqV2npc")
                                 .releaseDate(LocalDate.of(2024, 9, 7))
                                 .status(MovieStatus.ACTIVE)
                                 .genres(List.of(comedy, romance))
                                 .actors(List.of(nicoleGarciaActor, gerardDepardieuActor))
                                 .directors(List.of(alainResnaisDirector))
                                 .build()
                    );
                    Movie xuyenKhongCaiMenhGiaTocMovie = movieRepository.save(
                            Movie.builder()
                                 .code("MV000005")
                                 .title("Xuyên Không Cải Mệnh Gia Tộc")
                                 .duration(103)
                                 .summary(
                                         "Xuyên Không Cải Mệnh Gia Tộc xoay quanh Kie (Mint Ranchrawee), một sinh viên ngành khảo cổ học, sinh ra trong một gia đình gốc Hoa tại khu phố Tàu. Ngay từ nhỏ, Kie đã chứng kiến gia đình liên tục dính xui xẻo, ngồi không cũng gặp chuyện. Trong một lần cầu nguyện giải xui tại ngôi đền trong xóm, cô được thầy cúng tại đây cho biết, vận xui gia đình cô là nghiệp chướng do Kung (Alek Teeradetch) - ông tổ nhà cô gây ra. Dù không tin lắm vào trò mê tín, Kie nhờ thầy cúng giúp mình thực hiện nghi lễ thanh tẩy nghiệp chướng, cô vô tình bị đẩy về quá khứ và nhập xác vào người anh em thân thiết nhất của ông cố mình - Tai do March Chutavuth thủ vai. Từ đây, hàng loạt tình huống giở khóc giở cười liên tục ập đến, đồng thời những bí mật đen tối từ quá khứ đã dần được hé lộ.")
                                 .ageRating(AgeRating.T16)
                                 .country("Thái Lan")
                                 .rating(0)
                                 .imageLandscape("")
                                 .slug("chinatown-cha-cha")
                                 .imagePortrait(
                                         "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fchinatown-cha-cha-500_1724657094609.jpg?alt=media")
                                 .trailer("https://www.youtube.com/watch?v=-oZaO57RAAc")
                                 .releaseDate(LocalDate.of(2024, 9, 6))
                                 .status(MovieStatus.ACTIVE)
                                 .genres(List.of(comedy, action))
                                 .actors(List.of(
                                         teeradetchMetawarayutActor,
                                         chutavuthPattarakampolActor,
                                         ranchraweeUakoolwarawatActor
                                 ))
                                 .directors(List.of(jaturongMokjokDirector))
                                 .producers(List.of(velaEntertainmentProducer))
                                 .build()
                    );
                    Movie conNhotMotChongMovie = movieRepository.save(
                            Movie.builder()
                                 .code("MV000006")
                                 .title("Con Nhót Mót Chồng")
                                 .ageRating(AgeRating.T16)
                                 .duration(112)
                                 .summary(
                                         "Lấy cảm hứng từ web drama Chuyện Xóm Tui, phiên bản điện ảnh sẽ mang một màu sắc hoàn toàn khác: hài hước hơn, gần gũi và nhiều cảm xúc hơn. Bộ phim là câu chuyện của Nhót - người phụ nữ “chưa kịp già” đã sắp bị mãn kinh, vội vàng đi tìm chồng. Nhưng sâu thẳm trong cô là khao khát muốn có một đứa con, và luôn muốn hàn gắn với người cha suốt ngày say xỉn của mình.")
                                 .country("Việt Nam")
                                 .rating(0)
                                 .imageLandscape("")
                                 .slug("con-nhot-mot-chong")
                                 .imagePortrait(
                                         "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fcnmc-500_1704869791399.jpg?alt=media")
                                 .trailer("https://www.youtube.com/watch?v=e7KHOQ-alqY")
                                 .releaseDate(LocalDate.of(2024, 9, 4))
                                 .status(MovieStatus.ACTIVE)
                                 .genres(List.of(comedy, psychological))
                                 .actors(List.of(thaiHoaActor, thuTrangActor, tienLuatActor))
                                 .directors(List.of(vuNgocDangDirector))
                                 .producers(List.of(
                                         galaxyPlayProducer,
                                         galaxyStudioProducer,
                                         thuTrangEntertainmentProducer
                                 ))
                                 .build()
                    );

                    Movie quyAnMovie = movieRepository.save(
                            Movie.builder()
                                 .code("MV000007")
                                 .title("Quỷ Án")
                                 .duration(98)
                                 .summary(
                                         "Oddity/ Quỷ Án kể về vụ án người phụ nữ Dani bị sát hại dã man tại ngôi nhà mà vợ chồng cô đang sửa sang ở vùng nông thôn hẻo lánh. Chồng cô - Ted đang làm bác sĩ tại bệnh viện tâm thần. Mọi nghi ngờ đổ dồn vào một bệnh nhân tại đây. Không may, nghi phạm đã chết. Một năm sau, em gái mù của Dani ghé tới. Darcy là nhà ngoại cảm tự xưng, mang theo nhiều món đồ kì quái. Cô đến nhà Ted để tìm chân tướng về cái chết của chị gái.")
                                 .ageRating(AgeRating.T16)
                                 .country("Ireland")
                                 .rating(0)
                                 .imageLandscape("")
                                 .slug("oddity")
                                 .imagePortrait(
                                         "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Foddity-1_1725523860091.jpg?alt=media")
                                 .trailer("https://www.youtube.com/watch?v=2DmOv-pM1bM&t=2s")
                                 .releaseDate(LocalDate.of(2024, 9, 13))
                                 .status(MovieStatus.ACTIVE)
                                 .genres(List.of(horror, mystery))
                                 .actors(List.of(johnnyFrenchActor, carolynBrackenActor))
                                 .directors(List.of(damianMcCarthyDirector))
                                 .build()
                    );

                    Movie maDaMovie = movieRepository.save(
                            Movie.builder()
                                 .code("MV000008")
                                 .title("Ma Da")
                                 .duration(94)
                                 .summary(
                                         "Phim kể về hành trình của bà Lệ, người làm nghề vớt xác người chết trên sông để đưa về với gia đình. Trong quá trình làm nghề, bà Lệ đắc tội với Ma Da, một oan hồn sống dưới sông nước thường xuyên kéo chân người để thế mạng cho mình đi đầu thai. Ân oán của cả hai khiến cho Ma Da bắt mất bé Nhung, con gái của bà Lệ. Bà Lệ phải nhờ đến sự giúp đỡ của những người bên cạnh để cùng nhau lên đường tìm cách cứu bé Nhung và mở ra những bí mật đằng sau oan hồn Ma Da kia.")
                                 .country("Việt Nam")
                                 .rating(0)
                                 .imageLandscape("")
                                 .slug("ma-da")
                                 .imagePortrait(
                                         "https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/movies%2Fma-da-500_1723799717930.jpg?alt=media")
                                 .trailer("https://www.youtube.com/watch?v=l0E3vhaG9i4&t=1s")
                                 .releaseDate(LocalDate.of(2024, 8, 15))
                                 .status(MovieStatus.ACTIVE)
                                 .genres(List.of(horror))
                                 .actors(List.of(vietHuongActor, trungDanActor, nsutThanhLocActor))
                                 .directors(List.of(nguyenHuuHoangDirector))
                                 .build()
                    );


                    if (showTimeRepository.count() == 0) {
                        /* Today */
                        // B&Q Quang Trung

                        lamGiauVoiMaShowTime = showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room2QuangTrungCinema)
                                        .startDate(currentDate)
                                        .startTime(LocalTime.of(9, 30))
                                        .endTime(LocalTime.of(11, 30))
                                        .build()
                        );

                        maDaShowTime = showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(maDaMovie)
                                        .room(room1QuangTrungCinema)
                                        .startDate(currentDate)
                                        .startTime(LocalTime.of(8, 0))
                                        .endTime(LocalTime.of(9, 50))
                                        .build()
                        );

                        quyAnShowTime = showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(quyAnMovie)
                                        .room(room4QuangTrungCinema)
                                        .startDate(currentDate)
                                        .startTime(LocalTime.of(8, 0))
                                        .endTime(LocalTime.of(9, 55))
                                        .build()
                        );

                        matBiecShowTime = showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(matBiecMovie)
                                        .room(room4QuangTrungCinema)
                                        .startDate(currentDate)
                                        .startTime(LocalTime.of(10, 0))
                                        .endTime(LocalTime.of(12, 0))
                                        .build()
                        );

                        xuyenKhongCaiManhGiaTocShowTime = showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(xuyenKhongCaiMenhGiaTocMovie)
                                        .room(room4QuangTrungCinema)
                                        .startDate(currentDate)
                                        .startTime(LocalTime.of(12, 5))
                                        .endTime(LocalTime.of(13, 50))
                                        .build()
                        );

                        ongChuNguoiMyCuaToiShowTime = showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(ongChuNguoiMyCuaToiMovie)
                                        .room(room4QuangTrungCinema)
                                        .startDate(currentDate)
                                        .startTime(LocalTime.of(16, 5))
                                        .endTime(LocalTime.of(18, 10))
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
                                        .status(BaseStatus.INACTIVE)
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

                        deadpoolShowTime = showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(deadpoolMovie)
                                        .room(room3QuangTrungCinema)
                                        .startDate(currentDate)
                                        .startTime(LocalTime.of(10, 30))
                                        .endTime(LocalTime.of(12, 30))
                                        .build()
                        );

                        // movie in Nguyen Du Cinema
                        deadpoolShowTime_NguyenDuCinema = showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(nguyenDuCinema)
                                        .movie(deadpoolMovie)
                                        .room(room1NguyenDuCinema)
                                        .startDate(currentDate)
                                        .startTime(LocalTime.of(8, 0))
                                        .endTime(LocalTime.of(10, 10))
                                        .build()
                        );

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

                        // Sala Cinema
                        conNhotMotChongShowTime_SalaCinema = showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(salaCinema)
                                        .movie(conNhotMotChongMovie)
                                        .room(room1SalaCinema)
                                        .startDate(currentDate)
                                        .startTime(LocalTime.of(8, 0))
                                        .endTime(LocalTime.of(9, 55))
                                        .build()
                        );

                        /* Tomorrow */
                        // Quang Trung Cinema
                        showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room2QuangTrungCinema)
                                        .startDate(tomorrowDate)
                                        .startTime(LocalTime.of(10, 0))
                                        .endTime(LocalTime.of(12, 0))
                                        .build()
                        );

                        showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room2QuangTrungCinema)
                                        .startDate(tomorrowDate)
                                        .startTime(LocalTime.of(12, 15))
                                        .endTime(LocalTime.of(14, 15))
                                        .build()
                        );

                        showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room3QuangTrungCinema)
                                        .startDate(tomorrowDate)
                                        .startTime(LocalTime.of(14, 30))
                                        .endTime(LocalTime.of(16, 30))
                                        .build()
                        );

                        showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room3QuangTrungCinema)
                                        .startDate(tomorrowDate)
                                        .startTime(LocalTime.of(16, 45))
                                        .endTime(LocalTime.of(18, 45))
                                        .build()
                        );

                        // Nguyen Du Cinema
                        showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(nguyenDuCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room1NguyenDuCinema)
                                        .startDate(tomorrowDate)
                                        .startTime(LocalTime.of(11, 45))
                                        .endTime(LocalTime.of(13, 45))
                                        .build()
                        );

                        showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(nguyenDuCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room2NguyenDuCinema)
                                        .startDate(tomorrowDate)
                                        .startTime(LocalTime.of(15, 0))
                                        .endTime(LocalTime.of(17, 0))
                                        .build()
                        );

                        /* Today plus 2 */
                        // Quang Trung Cinema
                        showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room2QuangTrungCinema)
                                        .startDate(todayPlus2Days)
                                        .startTime(LocalTime.of(10, 0))
                                        .endTime(LocalTime.of(12, 0))
                                        .build()
                        );

                        showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room2QuangTrungCinema)
                                        .startDate(tomorrowDate)
                                        .startTime(LocalTime.of(12, 15))
                                        .endTime(LocalTime.of(14, 15))
                                        .build()
                        );

                        showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(quangTrungCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room3QuangTrungCinema)
                                        .startDate(tomorrowDate)
                                        .startTime(LocalTime.of(14, 30))
                                        .endTime(LocalTime.of(16, 30))
                                        .build()
                        );

                        // Nguyen Du Cinema
                        showTimeRepository.save(
                                ShowTime.builder()
                                        .cinema(nguyenDuCinema)
                                        .movie(lamGiauVoiMaMovie)
                                        .room(room1NguyenDuCinema)
                                        .startDate(todayPlus2Days)
                                        .startTime(LocalTime.of(11, 30))
                                        .endTime(LocalTime.of(13, 30))
                                        .build()
                        );
                    }
                }
            }
        }

        insertProducts();

        if (roleRepository.count() == 0) {
            Role roleAdmin = roleRepository.save(new Role("ROLE_ADMIN", "Quản trị viên"));
            Role roleClient = roleRepository.save(new Role(ROLE_CLIENT, "Khách hàng"));
            Role roleEmployeeSale = roleRepository.save(new Role("ROLE_EMPLOYEE_SALE", "Nhân viên bán hàng"));

            if (userRepository.count() == 0) {
                User client1 = userRepository.save(User.builder()
                                                     .code("USER00000001")
                                                     .name("Lê Hữu Bằng")
                                                     .phone("0837699806")
                                                     .avatar("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/avatar%2F1.jpg?alt=media")
                                                     .birthday(LocalDate.of(2002, 10, 10))
                                                     .email("huubangle20002@gmail.com")
                                                     .password(passwordEncoder.encode("Cinema123123@"))
                                                     .gender(true)
                                                     .role(roleClient)
                                                     .status(UserStatus.ACTIVE)
                                                     .build());

                User client2 = userRepository.save(User.builder()
                                                       .code("USER00000002")
                                                       .name("Lê Hữu A")
                                                       .phone("0837699807")
                                                       .avatar("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/avatar%2F1.jpg?alt=media")
                                                       .birthday(LocalDate.of(2002, 10, 10))
                                                       .email("huubangle20001@gmail.com")
                                                       .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                                                       .gender(false)
                                                       .role(roleClient)
                                                       .status(UserStatus.ACTIVE)
                                                       .build());

                User admin1 = userRepository.save(User.builder()
                                                     .code("NV00000001")
                                                     .name("Nguyễn Minh Quân")
                                                     .phone("0354927402")
                                                     .avatar("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/avatar%2Favt-1.png?alt=media")
                                                     .birthday(LocalDate.of(2002, 10, 10))
                                                     .email("quannguyenminh1001@gmail.com")
                                                     .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                                                     .gender(true)
                                                     .role(roleAdmin)
                                                     .status(UserStatus.ACTIVE)
                                                     .build());

                User nvbh1 = userRepository.save(User.builder()
                                                     .code("NVBH00000001")
                                                     .name("Nguyễn Văn A")
                                                     .phone("0354927403")
                                                     .avatar("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/avatar%2F3.png?alt=media")
                                                     .birthday(LocalDate.of(2002, 10, 10))
                                                     .email("quannguyenminh1002@gmail.com")
                                                     .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                                                     .gender(true)
                                                     .role(roleEmployeeSale)
                                                     .status(UserStatus.ACTIVE)
                                                     .build());

                User nvbh2 = userRepository.save(User.builder()
                                                     .code("NVBH00000002")
                                                     .name("Nguyễn Văn B")
                                                     .phone("0354927404")
                                                     .avatar("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/avatar%2F3.png?alt=media")
                                                     .birthday(LocalDate.of(2002, 10, 10))
                                                     .email("quannguyenminh1004@gmail.com")
                                                     .password(passwordEncoder.encode(DEFAULT_PASSWORD))
                                                     .gender(true)
                                                     .role(roleEmployeeSale)
                                                     .status(UserStatus.ACTIVE)
                                                     .build());

                insertOrders(client1, 404, 405, 309000, OrderStatus.COMPLETED, nvbh1, lamGiauVoiMaShowTime);
                insertOrders(client1, 406, null, 209000, OrderStatus.COMPLETED, admin1, lamGiauVoiMaShowTime);
                insertOrders(client1, 1318, null, 209000, OrderStatus.COMPLETED, nvbh2, deadpoolShowTime);
                insertOrders(client2, 1320, null, 209000, OrderStatus.COMPLETED, client2, deadpoolShowTime);
                insertOrders(client2, 1280, null, 209000, OrderStatus.COMPLETED, client2, maDaShowTime);
                insertOrders(client2, 1501, null, 209000, OrderStatus.COMPLETED, client2, quyAnShowTime);
                insertOrders(client2, 1502, null, 209000, OrderStatus.COMPLETED, client2, quyAnShowTime);
                insertOrders(client2, 1503, null, 209000, OrderStatus.COMPLETED, nvbh2, quyAnShowTime);
                insertOrders(client1, 1501, 1502, 309000, OrderStatus.COMPLETED, client1, matBiecShowTime);
                insertOrders(client1, 1503, null, 209000, OrderStatus.COMPLETED, nvbh1, matBiecShowTime);
                insertOrders(client1, 1504, null, 209000, OrderStatus.COMPLETED, client1, matBiecShowTime);
                insertOrders(client1, 1504, null, 209000, OrderStatus.COMPLETED, client1, xuyenKhongCaiManhGiaTocShowTime);
                insertOrders(client1, 1501, 1502, 309000, OrderStatus.COMPLETED, nvbh1, ongChuNguoiMyCuaToiShowTime);
                insertOrders(client1, 107, 108, 309000, OrderStatus.COMPLETED, nvbh1, deadpoolShowTime_NguyenDuCinema);
                insertOrders(client2, 87, 88, 309000, OrderStatus.COMPLETED, nvbh2, deadpoolShowTime_NguyenDuCinema);
                insertOrders(client2, 402, 403, 309000, OrderStatus.COMPLETED, nvbh2, conNhotMotChongShowTime_SalaCinema);
                Order order = insertOrders(client1, 408, null, 209000, OrderStatus.CANCELLED, admin1, lamGiauVoiMaShowTime);

                Refund refund = Refund.builder()
                                      .order(order)
                                      .code("HDHT00000001")
                                      .amount(order.getFinalAmount())
                                      .refundMethod(RefundMethod.CASH)
                                      .reason("Không thể đến xem phim")
                                      .refundDate(LocalDateTime.now())
                                      .status(RefundStatus.COMPLETED)
                                      .build();
                refundRepository.save(refund);
            }
        }
        insertTicketPrices();
        insertPromotion();


        promotionLineRepository.findActivePromotionLine(LocalDate.now()).forEach(promotionLine -> {
            log.info("Promotion line: {}", promotionLine);
        });
    }

    private void insertPromotion() {
        Promotion promotion = promotionRepository.save(
                Promotion.builder()
                         .name("Khuyến mãi tháng " + LocalDate.now().getMonthValue() + " năm " + LocalDate.now()
                                                                                                          .getYear())
                         .startDate(currentDate.withDayOfMonth(1))
                         .endDate(YearMonth.now().atEndOfMonth())
                         .code("KM" + currentDate.getMonthValue() + currentDate.getYear())
                         .status(BaseStatus.ACTIVE)
                         .build()
        );

        promotionRepository.save(
                Promotion.builder()
                         .name("Khuyến mãi tháng " + LocalDate.now().plusMonths(1)
                                                              .getMonthValue() + " năm " + LocalDate.now().plusMonths(1)
                                                                                                    .getYear())
                         .startDate(currentDate.plusMonths(1).withDayOfMonth(1))
                         .endDate(YearMonth.now().plusMonths(1).atEndOfMonth())
                         .code("KM" + currentDate.plusMonths(1).getMonthValue() + currentDate.plusMonths(1).getYear())
                         .status(BaseStatus.INACTIVE)
                         .build()
        );

        PromotionLine promotionLine = promotionLineRepository.save(
                PromotionLine.builder()
                             .promotion(promotion)
                             .name("Giảm giá chào đón thành viên mới")
                             .code("CHAOBANMOI")
                             .startDate(currentDate)
                             .endDate(YearMonth.now().atEndOfMonth())
                             .type(PromotionLineType.CASH_REBATE)
                             .status(BaseStatus.ACTIVE)
                             .build()
        );

        promotionDetailRepository.save(
                PromotionDetail.builder()
                               .promotionLine(promotionLine)
                               .discountValue(50000)
                               .minOrderValue(100000)
                               .usageLimit(50)
                               .currentUsageCount(0)
                               .status(BaseStatus.ACTIVE)
                               .build()
        );

        promotionDetailRepository.save(
                PromotionDetail.builder()
                               .promotionLine(promotionLine)
                               .discountValue(100000)
                               .minOrderValue(200000)
                               .usageLimit(50)
                               .currentUsageCount(0)
                               .status(BaseStatus.ACTIVE)
                               .build()
        );

        PromotionLine promotionLine1 = promotionLineRepository.save(
                PromotionLine.builder()
                             .promotion(promotion)
                             .name("Kỷ niệm 10 năm")
                             .code("SALE10")
                             .startDate(currentDate)
                             .endDate(YearMonth.now().atEndOfMonth())
                             .type(PromotionLineType.PRICE_DISCOUNT)
                             .status(BaseStatus.ACTIVE)
                             .build()
        );

        promotionDetailRepository.save(
                PromotionDetail.builder()
                               .promotionLine(promotionLine1)
                               .discountValue(10)
                               .maxDiscountValue(50000F)
                               .minOrderValue(100000)
                               .usageLimit(50)
                               .currentUsageCount(0)
                               .status(BaseStatus.ACTIVE)
                               .build()
        );

        PromotionLine promotionLine3 = promotionLineRepository.save(
                PromotionLine.builder()
                             .promotion(promotion)
                             .name("Combo 1 vé xem phim + 1 sản phẩm")
                             .code("1VETANG1COMBO")
                             .startDate(currentDate)
                             .endDate(YearMonth.now().atEndOfMonth())
                             .type(PromotionLineType.BUY_TICKETS_GET_PRODUCTS)
                             .status(BaseStatus.ACTIVE)
                             .build()
        );

        promotionDetailRepository.save(
                PromotionDetail.builder()
                               .promotionLine(promotionLine3)
                               .usageLimit(50)
                               .currentUsageCount(0)
                               .requiredSeatQuantity(2)
                               .requiredSeatType(SeatType.NORMAL)
                               .giftQuantity(1)
                               .giftProduct(product)
                               .status(BaseStatus.ACTIVE)
                               .build()
        );
    }

    private Order insertOrders(
            User user,
            int seatId1,
            Integer seatId2,
            float totalPrice,
            OrderStatus status,
            User createdBy,
            ShowTime show
    ) {
        Order order = orderRepository.save(
                Order.builder()
                     .orderDate(LocalDateTime.now().minusDays(1))
                     .code(generateOrderCode())
                     .paymentMethod(PaymentMethod.CASH)
                     .paymentStatus(PaymentStatus.PAID)
                     .totalPrice(100000)
                     .finalAmount(100000)
                     .showTime(show)
                     .totalPrice(totalPrice)
                     .finalAmount(totalPrice)
                     .user(user)
                     .status(status)
                     .createdBy(createdBy.getId())
                     .build()
        );

        orderDetailRepository.save(
                OrderDetail.builder()
                           .product(product)
                           .price(109000)
                           .quantity(1)
                           .order(order)
                           .type(OrderDetailType.PRODUCT)
                           .build()
        );

        Seat seat1 = seatRepository.findById(seatId1).get();

        orderDetailRepository.save(
                OrderDetail.builder()
                           .price(100000)
                           .seat(seat1)
                           .order(order)
                           .type(OrderDetailType.TICKET)
                           .build()
        );

        if (seatId2 != null) {
            Seat seat2 = seatRepository.findById(seatId2).get();
            orderDetailRepository.save(
                    OrderDetail.builder()
                               .price(100000)
                               .seat(seat2)
                               .order(order)
                               .type(OrderDetailType.TICKET)
                               .build()
            );

            lamGiauVoiMaShowTime.setBookedSeat(lamGiauVoiMaShowTime.getBookedSeat() + 2);
            showTimeRepository.save(lamGiauVoiMaShowTime);
        } else {
            lamGiauVoiMaShowTime.setBookedSeat(lamGiauVoiMaShowTime.getBookedSeat() + 1);
            showTimeRepository.save(lamGiauVoiMaShowTime);
        }
        return order;
    }

    private void insertTicketPrices() {
        TicketPrice month9_TicketPrice = ticketPriceRepository.save(
                TicketPrice.builder()
                           .startDate(currentDate)
                           .endDate(currentDate)
                           .name("Giá vé 2024")
                           .status(BaseStatus.ACTIVE)
                           .build()
        );

        TicketPriceLine ticketPriceLine1 = ticketPriceLineRepository.save(
                TicketPriceLine.builder()
                               .ticketPrice(month9_TicketPrice)
                               .applyForDays(List.of(DayType.MONDAY, DayType.WEDNESDAY, DayType.THURSDAY))
                               .startTime(LocalTime.of(0, 0))
                               .endTime(LocalTime.of(17, 0))
                               .audienceType(AudienceType.ADULT)
                               .build()
        );

        insertTicketPriceDetails(ticketPriceLine1, new float[]{75000, 90000, 130000});

        TicketPriceLine ticketPriceLine2 = ticketPriceLineRepository.save(
                TicketPriceLine.builder()
                               .ticketPrice(month9_TicketPrice)
                               .applyForDays(List.of(DayType.MONDAY, DayType.WEDNESDAY, DayType.THURSDAY))
                               .startTime(LocalTime.of(17, 1))
                               .endTime(LocalTime.of(23, 59))
                               .audienceType(AudienceType.ADULT)
                               .build()
        );

        insertTicketPriceDetails(ticketPriceLine2, new float[]{80000, 100000, 150000});

        TicketPriceLine ticketPriceLine3 = ticketPriceLineRepository.save(
                TicketPriceLine.builder()
                               .ticketPrice(month9_TicketPrice)
                               .applyForDays(List.of(DayType.TUESDAY))
                               .startTime(LocalTime.of(0, 0))
                               .endTime(LocalTime.of(23, 59))
                               .audienceType(AudienceType.ADULT)
                               .build()
        );

        insertTicketPriceDetails(ticketPriceLine3, new float[]{55000, 60000, 100000});

        TicketPriceLine ticketPriceLine4 = ticketPriceLineRepository.save(
                TicketPriceLine.builder()
                               .ticketPrice(month9_TicketPrice)
                               .applyForDays(List.of(DayType.FRIDAY, DayType.SATURDAY, DayType.SUNDAY))
                               .startTime(LocalTime.of(0, 0))
                               .endTime(LocalTime.of(17, 0))
                               .audienceType(AudienceType.ADULT)
                               .build()
        );

        insertTicketPriceDetails(ticketPriceLine4, new float[]{85000, 95000, 160000});

        TicketPriceLine ticketPriceLine5 = ticketPriceLineRepository.save(
                TicketPriceLine.builder()
                               .ticketPrice(month9_TicketPrice)
                               .applyForDays(List.of(DayType.FRIDAY, DayType.SATURDAY, DayType.SUNDAY))
                               .startTime(LocalTime.of(17, 1))
                               .endTime(LocalTime.of(23, 59))
                               .audienceType(AudienceType.ADULT)
                               .build()
        );

        insertTicketPriceDetails(ticketPriceLine5, new float[]{95000, 105000, 180000});

        TicketPrice ticketPrice2025_1 = ticketPriceRepository.save(
                TicketPrice.builder()
                           .startDate(LocalDate.of(2025, 1, 1))
                           .endDate(LocalDate.of(2025, 12, 31))
                           .name("Giá vé 2025")
                           .status(BaseStatus.INACTIVE)
                           .build()
        );

        TicketPriceLine ticketPrice2025_Monday = ticketPriceLineRepository.save(
                TicketPriceLine.builder()
                               .ticketPrice(ticketPrice2025_1)
                               .applyForDays(List.of(DayType.MONDAY))
                               .startTime(LocalTime.of(0, 0))
                               .endTime(LocalTime.of(13, 59))
                               .audienceType(AudienceType.ADULT)
                               .build()
        );

        insertTicketPriceDetails(ticketPrice2025_Monday, new float[]{95000, 105000, 180000});

        TicketPrice ticketPrice2025_2 = ticketPriceRepository.save(
                TicketPrice.builder()
                           .startDate(LocalDate.of(2025, 1, 1))
                           .endDate(LocalDate.of(2025, 12, 31))
                           .name("Giá vé 2025 bổ sung")
                           .status(BaseStatus.INACTIVE)
                           .build()
        );

        TicketPriceLine ticketPrice2025_2_Monday = ticketPriceLineRepository.save(
                TicketPriceLine.builder()
                               .ticketPrice(ticketPrice2025_2)
                               .applyForDays(List.of(DayType.MONDAY))
                               .startTime(LocalTime.of(14, 0))
                               .endTime(LocalTime.of(23, 59))
                               .audienceType(AudienceType.ADULT)
                               .build()
        );

        insertTicketPriceDetails(ticketPrice2025_2_Monday, new float[]{55000, 95000, 170000});
    }

    private void insertTicketPriceDetails(TicketPriceLine line, float[] prices) {
        ticketPriceDetailRepository.save(
                TicketPriceDetail.builder()
                                 .seatType(SeatType.NORMAL)
                                 .ticketPriceLine(line)
                                 .price(prices[0])
                                 .build()
        );

        ticketPriceDetailRepository.save(
                TicketPriceDetail.builder()
                                 .seatType(SeatType.VIP)
                                 .ticketPriceLine(line)
                                 .price(prices[1])
                                 .build()
        );

        ticketPriceDetailRepository.save(
                TicketPriceDetail.builder()
                                 .seatType(SeatType.COUPLE)
                                 .ticketPriceLine(line)
                                 .price(prices[2])
                                 .build()
        );
    }

    private void insertLayout(int[][][] layout, Room room, int totalSeat) {
        int maxRow = layout.length;
        int maxColumn = layout[0].length;
        RoomLayout roomLayout = roomLayoutRepository.save(
                RoomLayout.builder()
                          .room(room)
                          .maxRow(maxRow)
                          .maxColumn(maxColumn)
                          .totalSeat(totalSeat)
                          .build()
        );

        RowSeat row;
        Seat seat;
        int count = 0;
        int maxRowData = 0;

        for (int[][] ints : layout) {
            if (ints.length != 0) {
                maxRowData++;
            }
        }

        for (int i = 0; i < maxRow; i++) {
            if (layout[i].length == 0) {
                continue;
            }

            String name = String.valueOf((char) ('A' + maxRowData - 1 - (count++)));

            row = rowSeatRepository.save(
                    RowSeat.builder()
                           .index((short) i)
                           .name(name)
                           .layout(roomLayout)
                           .build()
            );

            for (int j = 0; j < maxColumn; j++) {
                if (layout[i][j][0] == 0) {
                    continue;
                }

                if (layout[i][j].length > 1) {
                    seat = seatRepository.save(
                            Seat.builder()
                                .area((short) layout[i][j].length)
                                .name((short) layout[i][j][0])
                                .fullName(name + layout[i][j][0] + " " + name + layout[i][j][1])
                                .rowIndex((short) i)
                                .columnIndex((short) j)
                                .type(SeatType.COUPLE)
                                .row(row)
                                .build()
                    );

                    groupSeatRepository.save(
                            GroupSeat.builder()
                                     .area((short) 1)
                                     .columnIndex((short) j)
                                     .rowIndex((short) i)
                                     .seat(seat)
                                     .build()
                    );

                    if (layout[i][j][1] > layout[i][j][0]) {
                        groupSeatRepository.save(
                                GroupSeat.builder()
                                         .area((short) 1)
                                         .columnIndex((short) (j - 1))
                                         .rowIndex((short) i)
                                         .seat(seat)
                                         .build()
                        );
                    } else {
                        groupSeatRepository.save(
                                GroupSeat.builder()
                                         .area((short) 1)
                                         .columnIndex((short) (j + 1))
                                         .rowIndex((short) i)
                                         .seat(seat)
                                         .build()
                        );
                    }
                } else {
                    seatRepository.save(
                            Seat.builder()
                                .area((short) layout[i][j].length)
                                .name((short) layout[i][j][0])
                                .fullName(name + layout[i][j][0])
                                .rowIndex((short) i)
                                .columnIndex((short) j)
                                .type(SeatType.NORMAL)
                                .row(row)
                                .build()
                    );
                }
            }
        }
    }

    private void insertProducts() {
        Product combo1 = productRepository.save(
                Product.builder()
                       .code("CB000001")
                       .name("iCombo 1 Big Extra STD (new)")
                       .description("1 Ly nước ngọt size L + 01 Hộp bắp + 1 Snack")
                       .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/products%2Fmenuboard-coonline-2024-combo1-min_1711699834430.jpg?alt=media")
                       .status(ProductStatus.INACTIVE)
                       .build()
        );

        productPriceRepository.save(
                ProductPrice.builder()
                            .price(109000)
                            .product(combo1)
                            .startDate(currentDate)
                            .endDate(currentDate.plusYears(1))
                            .status(BaseStatus.ACTIVE)
                            .build()
        );

        productPriceRepository.save(
                ProductPrice.builder()
                            .price(100000)
                            .product(combo1)
                            .startDate(currentDate.plusYears(1))
                            .endDate(currentDate.plusYears(2))
                            .status(BaseStatus.INACTIVE)
                            .build()
        );

        Product combo2 = productRepository.save(
                Product.builder()
                       .code("CB000002")
                       .name("iCombo 1 Big STD")
                       .description("01 Ly nước ngọt size L + 01 Hộp bắp")
                       .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/products%2Fmenuboard-coonline-2024-combo1-copy-min_1711699814762.jpg?alt=media")
                       .status(ProductStatus.ACTIVE)
                       .build()
        );

        productPriceRepository.save(
                ProductPrice.builder()
                            .price(89000)
                            .product(combo2)
                            .startDate(currentDate)
                            .endDate(currentDate.plusYears(1))
                            .status(BaseStatus.ACTIVE)
                            .build()
        );

        Product combo3 = productRepository.save(
                Product.builder()
                       .code("CB000003")
                       .name("iCombo 2 Big Extra STD")
                       .description("02 Ly nước ngọt size L + 01 Hộp bắp + 1 Snack")
                       .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/products%2Fmenuboard-coonline-2024-combo2-min_1711699866349.jpg?alt=media")
                       .status(ProductStatus.ACTIVE)
                       .build()
        );

        productPriceRepository.save(
                ProductPrice.builder()
                            .price(129000)
                            .product(combo3)
                            .startDate(currentDate)
                            .endDate(currentDate.plusYears(1))
                            .status(BaseStatus.ACTIVE)
                            .build()
        );

        product = productRepository.save(
                Product.builder()
                       .code("CB000004")
                       .name("iCombo 2 Big STD")
                       .description("02 Ly nước ngọt size L + 01 Hộp bắp")
                       .image("https://firebasestorage.googleapis.com/v0/b/cinema-782ef.appspot.com/o/products%2Fmenuboard-coonline-2024-combo2-copy-min_1711699849615.jpg?alt=media")
                       .status(ProductStatus.ACTIVE)
                       .build()
        );

        productPriceRepository.save(
                ProductPrice.builder()
                            .price(109000)
                            .product(product)
                            .startDate(currentDate)
                            .endDate(currentDate.plusYears(1))
                            .status(BaseStatus.ACTIVE)
                            .build()
        );

    }

    private void insertRooms(Cinema cinema, int[][][] layout1, int[][][] layout2, int[][][] layout3) {
        Room room1 = roomRepository.save(
                Room.builder()
                    .name("Phòng 1")
                    .cinema(cinema)
                    .build()
        );

        insertLayout(layout1, room1, 77);

        Room room2 = roomRepository.save(
                Room.builder()
                    .name("Phòng 2")
                    .cinema(cinema)
                    .build()
        );

        insertLayout(layout2, room2, 21);

        Room room3 = roomRepository.save(
                Room.builder()
                    .name("Phòng 3")
                    .cinema(cinema)
                    .build()
        );

        insertLayout(layout3, room3, 112);

        Room room4 = roomRepository.save(
                Room.builder()
                    .name("Phòng 4")
                    .cinema(cinema)
                    .build()
        );

        insertLayout(layout1, room4, 77);
    }

    private void initializeCinemas() {

    }

    private String generateOrderCode() {
        long orderCount = orderRepository.count();
        LocalDateTime now = LocalDateTime.now();
        return String.format("HD%s%06d", now.format(DateTimeFormatter.ofPattern("yyMMdd")), (orderCount % 1000000) + 1);
    }
}

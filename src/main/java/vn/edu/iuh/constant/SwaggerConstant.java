package vn.edu.iuh.constant;

public class SwaggerConstant {

    public static class AdminSwagger {
        public static class Actor {
            public static final String CREATE_SUM = "Tạo diễn viên";
            public static final String GET_ALL_SUM = "Lấy danh sách diễn viên";
            public static final String GET_SUM = "Lấy thông tin diễn viên";
            public static final String UPDATE_SUM = "Cập nhật diễn viên";
            public static final String DELETE_SUM = "Xóa diễn viên";
        }

        public static class Auth {
            public static final String LOGIN_SUM = "Đăng nhập";
            public static final String PROFILE_SUM = "Thông tin người dùng";
            public static final String FORGOT_PASSWORD_SUM = "Quên mật khẩu";
            public static final String LOGOUT_SUM = "Đăng xuất";
            public static final String CHANGE_PASSWORD_SUM = "Đổi mật khẩu";
        }

        public static class Callback {
            public static final String ZALOPAY_SUM = "Callback cho ZaloPay";
        }

        public static class Director {
            public static final String CREATE_SUM = "Tạo đạo diễn";
            public static final String GET_ALL_SUM = "Lấy danh sách đạo diễn";
            public static final String GET_SUM = "Lấy thông tin đạo diễn";
            public static final String UPDATE_SUM = "Cập nhật đạo diễn";
            public static final String DELETE_SUM = "Xóa đạo diễn";
        }

        public static class Movie {
            public static final String CREATE_SUM = "Tạo phim";
            public static final String GET_ALL_SUM = "Lấy danh sách phim";
            public static final String GET_SUM = "Lấy thông tin phim";
            public static final String GET_FOR_SALE_SUM = "Lấy danh sách phim cho bán vé";
            public static final String FILTER_SUM = "Lấy bộ lọc phim";
            public static final String UPDATE_SUM = "Cập nhật phim";
            public static final String DELETE_SUM = "Xóa phim";
        }

        public static class Payment {
            public static final String CREATE_ORDER_ZALOPAY_SUM = "Tạo đơn hàng ZaloPay";
            public static final String STATUS_ORDER_ZALOPAY_SUM = "Lấy thông tin đơn hàng ZaloPay";
        }

        public static class Promotion {
            public static final String CREATE_SUM = "Tạo chiến dịch khuyến mãi";
            public static final String CREATE_LINES_SUM = "Tạo chương trình khuyến mãi";
            public static final String GET_ALL_SUM = "Lấy danh sách chiến dịch khuyến mãi";
            public static final String GET_SUM = "Lấy thông tin chiến dịch khuyến mãi";
            public static final String GET_ALL_LINES_SUM = "Lấy danh sách chương trình khuyến mãi";
            public static final String UPDATE_SUM = "Cập nhật chiến dịch khuyến mãi";
            public static final String DELETE_SUM = "Xóa chiến dịch khuyến mãi";
        }

        public static class Product {
            public static final String CREATE_SUM = "Tạo sản phẩm";
            public static final String GET_ALL_SUM = "Lấy danh sách sản phẩm";
            public static final String GET_ALL_ACTIVE_SUM = "Lấy danh sách sản phẩm hoạt động";
            public static final String GET_SUM = "Lấy thông tin sản phẩm";
            public static final String UPDATE_SUM = "Cập nhật sản phẩm";
            public static final String DELETE_SUM = "Xóa sản phẩm";
        }

        public static class ProductPrice {
            public static final String GET_ALL_SUM = "Lấy danh sách giá sản phẩm";
            public static final String GET_SUM = "Lấy thông tin giá sản phẩm";
            public static final String DELETE_SUM = "Xóa giá sản phẩm";
            public static final String CREATE_SUM = "Tạo giá sản phẩm";
            public static final String UPDATE_SUM = "Cập nhật giá sản phẩm";
        }

        public static class TicketPrice {
            public static final String GET_SUM = "Lấy thông tin bảng giá vé";
            public static final String GET_ALL_SUM = "Lấy bảng giá vé";
            public static final String DELETE_SUM = "Xóa bảng giá vé";
            public static final String CREATE_SUM = "Tạo bảng giá vé";
            public static final String UPDATE_SUM = "Cập nhật bảng giá vé";
            public static final String CREATE_LINES_SUM = "Tạo giá vé";
            public static final String UPDATE_LINES_SUM = "Cập nhật giá vé";
        }

        public static class Cinema {
            public static final String GET_ALL_SUM = "Lấy danh sách rạp";
            public static final String GET_SUM = "Lấy thông tin rạp";
            public static final String DELETE_SUM = "Xóa rạp";
            public static final String CREATE_SUM = "Tạo rạp";
            public static final String UPDATE_SUM = "Cập nhật rạp";
            public static final String GET_ROOMS_SUM = "Lấy danh sách phòng chiếu";
        }

        public static class Role {
            public static final String GET_ALL_SUM = "Lấy danh sách quyền";
        }

        public static class Room {
            public static final String CREATE_SUM = "Tạo phòng chiếu";
            public static final String GET_ALL_SUM = "Lấy danh sách phòng chiếu";
            public static final String GET_SUM = "Lấy thông tin phòng chiếu";
            public static final String UPDATE_SUM = "Cập nhật phòng chiếu";
            public static final String DELETE_SUM = "Xóa phòng chiếu";
        }

        public static class Refund {
            public static final String GET_ALL_SUM = "Lấy danh sách hóa đơn hoàn trả";
            public static final String GET_SUM = "Lấy thông tin hóa đơn hoàn trả";
        }

        public static class Report {
            public static final String GET_REPORT_EMPLOYEE_SALE_SUM = "Lấy báo cáo doanh số theo nhân viên bán hàng";
            public static final String GET_REPORT_MOVIE_SALE_SUM = "Lấy báo cáo doanh số theo phim";
            public static final String GET_REPORT_PROMOTION_SUM = "Lấy báo cáo khuyến mãi";
        }

        public static class ShowTime {
            public static final String CREATE_SUM = "Tạo lịch chiếu";

            public static final String GET_ALL_SUM = "Lấy danh sách lịch chiếu";
            public static final String GET_FOR_SALE_SUM = "Lấy lịch chiếu cho bán vé tại quầy";
            public static final String FILTER_SUM = "Lấy bộ lọc lịch chiếu";
            public static final String UPDATE_SUM = "Cập nhật lịch chiếu";

            public static final String ACTIVATE_MULTIPLE_SUM = "Kích hoạt nhiều lịch chiếu";
            public static final String DELETE_SUM = "Xóa lịch chiếu";
            public static final String GENERATE_SUM = "Tạo lịch chiếu tự động";
        }

        public static class Order {
            public static final String CREATE_SUM = "Tạo đơn hàng";
            public static final String GET_ALL_SUM = "Lấy danh sách đơn hàng";
            public static final String GET_SUM = "Lấy thông tin đơn hàng";
            public static final String UPDATE_PRODUCTS_SUM = "Cập nhật sản phẩm trong đơn hàng";
            public static final String UPDATE_SEATS_SUM = "Cập nhật ghế trong đơn hàng";
            public static final String UPDATE_CUSTOMER_SUM = "Cập nhật thông tin khách hàng trong đơn hàng";
            public static final String CANCEL_SUM = "Hủy đơn hàng";
            public static final String COMPLETE_SUM = "Hoàn thành đơn hàng";
            public static final String REFUND_SUM = "Hoàn đơn";
        }

        public static class Employee {
            public static final String CREATE_SUM = "Tạo nhân viên";
            public static final String GET_ALL_SUM = "Lấy danh sách nhân viên";
            public static final String GET_SUM = "Lấy thông tin nhân viên";
            public static final String DELETE_SUM = "Xóa nhân viên";
            public static final String UPDATE_SUM = "Cập nhật nhân viên";
        }
    }

    public static class ClientSwagger {

        public static class Order {
            public static final String CREATE_SUM = "Tạo đơn hàng";
            public static final String GET_SUM = "Lấy thông tin đơn hàng";
            public static final String UPDATE_PRODUCTS_SUM = "Cập nhật sản phẩm trong đơn hàng";
            public static final String UPDATE_SEATS_SUM = "Cập nhật ghế trong đơn hàng";
            public static final String CANCEL_SUM = "Hủy đơn hàng";
            public static final String APPLY_PROMOTION_SUM = "Áp mã khuyến mãi";
        }
    }

    // FOR ADMIN AUTH DESC
    public static final String POST_LOGIN_DESC = """
            Access token: 24 hours
            
            Refresh token: 14 days
            """;
}

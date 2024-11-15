package vn.edu.iuh.constant;

public class SwaggerConstant {
    // FOR ADMIN AUTH
    public static final String POST_LOGIN_SUM = "Đăng nhập";

    // FOR ADMIN REPORT
    public static final String GET_ADMIN_REPORT_DAILY_SUM = "Lấy báo cáo doanh thu theo ngày";
    public static final String GET_ADMIN_REPORT_PROMOTION_SUM = "Lấy báo cáo khuyến mãi";

    // FOR ADMIN CALLBACK
    public static final String POST_ADMIN_CALLBACK_ZALOPAY_SUM = "Callback cho ZaloPay";

    // FOR ADMIN PAYMENT
    public static final String POST_ADMIN_PAYMENT_ZALOPAY_SUM = "Tạo đơn hàng ZaloPay";
    public static final String GET_ADMIN_PAYMENT_ZALOPAY_SUM = "Lấy thông tin đơn hàng ZaloPay";

    // FOR ADMIN EMPLOYEE
    public static final String GET_ALL_ADMIN_EMPLOYEE_SUM = "Danh sách nhân viên";
    public static final String GET_ADMIN_EMPLOYEE_SUM = "Lấy thông tin nhân viên";
    public static final String POST_ADMIN_EMPLOYEE_SUM = "Thêm mới nhân viên";
    public static final String PUT_ADMIN_EMPLOYEE_SUM = "Cập nhật thông tin nhân viên";
    public static final String DELETE_ADMIN_EMPLOYEE_SUM = "Xóa nhân viên";

    // FOR SHOW TIME
    public static final String GET_SHOW_TIME_FOR_SALE_SUB_PATH_SUM = "Lấy lịch chiếu cho bán vé tại quầy";
    public static final String GET_SHOW_TIME_FOR_FILTER_SUB_PATH_SUM = "Lấy bộ lọc lịch chiếu";
    public static final String GET_SHOW_TIME_SUM = "Lấy tất cả lịch chiếu";
    public static final String POST_SHOW_TIME_SUM = "Thêm lịch chiếu";
    public static final String POST_SHOW_TIME_GENERATE_SUM = "Tạo lịch chiếu tự động";
    public static final String DELETE_SHOW_TIME_SUB_PATH_SUM = "Xóa lịch chiếu";
    public static final String PUT_SHOW_TIME_SUB_PATH_SUM = "Cập nhật lịch chiếu";

    // FOR ADMIN MOVIE
    public static final String GET_ADMIN_MOVIE_FOR_SALE_SUM = "Lấy danh sách phim cho bán vé";
    public static final String GET_ALL_ADMIN_MOVIE_SUM = "Lấy tất cả phim";
    public static final String GET_ADMIN_MOVIE_SUM = "Lấy thông tin phim";
    public static final String GET_ADMIN_MOVIE_FOR_FILTER_SUM = "Lấy thông tin bộ lọc phim";
    public static final String DELETE_ADMIN_MOVIE_SUM = "Xóa phim";
    public static final String POST_ADMIN_MOVIE_SUM = "Thêm phim";
    public static final String PUT_ADMIN_MOVIE_SUM = "Cập nhật phim";

    public static class AdminSwagger {
        public static class Actor {
            public static final String CREATE_SUM = "Tạo diễn viên";
            public static final String GET_ALL_SUM = "Lấy danh sách diễn viên";
            public static final String GET_SUM = "Lấy thông tin diễn viên";
            public static final String UPDATE_SUM = "Cập nhật diễn viên";
            public static final String DELETE_SUM = "Xóa diễn viên";
        }

        public static class Director {
            public static final String CREATE_SUM = "Tạo đạo diễn";
            public static final String GET_ALL_SUM = "Lấy danh sách đạo diễn";
            public static final String GET_SUM = "Lấy thông tin đạo diễn";
            public static final String UPDATE_SUM = "Cập nhật đạo diễn";
            public static final String DELETE_SUM = "Xóa đạo diễn";
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

        public static class Order {
            public static final String CREATE_SUM = "Tạo đơn hàng";
            public static final String GET_ALL_SUM = "Lấy danh sách đơn hàng";
            public static final String GET_SUM = "Lấy thông tin đơn hàng";
            public static final String UPDATE_PRODUCTS_SUM = "Cập nhật sản phẩm trong đơn hàng";
            public static final String UPDATE_SEATS_SUM = "Cập nhật ghế trong đơn hàng";
            public static final String CANCEL_SUM = "Hủy đơn hàng";
            public static final String COMPLETE_SUM = "Hoàn thành đơn hàng";
            public static final String REFUND_SUM = "Hoàn đơn";
        }
    }

    // FOR ADMIN AUTH DESC
    public static final String POST_LOGIN_DESC = """
            Access token: 24 hours
            
            Refresh token: 14 days
            """;
}

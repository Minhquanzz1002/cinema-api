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

    // FOR ADMIN AUTH DESC
    public static final String POST_LOGIN_DESC = """
            Access token: 24 hours
            
            Refresh token: 14 days
            """;
}

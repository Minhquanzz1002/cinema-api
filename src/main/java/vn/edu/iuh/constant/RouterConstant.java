package vn.edu.iuh.constant;

public class RouterConstant {
    // BASE ADMIN API PATH ====================================
    public static final String ADMIN_REPORT_BASE_PATH = "/admin/v1/reports";
    public static final String ADMIN_PAYMENT_BASE_PATH = "/admin/v1/payments";
    public static final String ADMIN_CALLBACK_BASE_PATH = "/admin/v1/callback";
    public static final String ADMIN_EMPLOYEE_BASE_PATH = "/admin/v1/employees";
    public static final String ADMIN_CINEMA_BASE_PATH = "/admin/v1/cinemas";
    public static final String ADMIN_ROOM_BASE_PATH = "/admin/v1/rooms";
    public static final String ADMIN_SHOWTIME_BASE_PATH = "/admin/v1/show-times";



    // BASE API PATH ==========================================
    public static final String PAYMENT_BASE_PATH = "/v1/payments";

    // ENDPOINT URL ADMIN REPORT ===============================
    public static final String GET_ADMIN_REPORT_DAILY_SUB_PATH = "/daily";

    // ENDPOINT URL ADMIN ZALOPAY PAYMENT ======================
    public static final String POST_ADMIN_ZALOPAY_SUB_PATH = "/zalo-pay";
    public static final String GET_ADMIN_ZALOPAY_SUB_PATH = "/zalo-pay";

    // ENDPOINT URL ADMIN CALLBACK =============================
    public static final String POST_ADMIN_CALLBACK_ZALOPAY_SUB_PATH = "/zalo-pay";

    // ENDPOINT URL ADMIN EMPLOYEE =============================
    public static final String GET_ADMIN_EMPLOYEE_SUB_PATH = "/employees";

    // ENDPOINT URL SHOW TIME =============================
    public static final String GET_SHOW_TIME_FOR_FILTER_SUB_PATH = "/filters";
    public static final String GET_SHOW_TIME_FOR_SALE_SUB_PATH = "/sales";
    public static final String POST_SHOW_TIME_GENERATE_SUB_PATH = "/generate";
    public static final String PUT_SHOW_TIME_SUB_PATH = "/{id}";
    public static final String DELETE_SHOW_TIME_SUB_PATH = "/{id}";
}

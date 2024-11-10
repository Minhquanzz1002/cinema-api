package vn.edu.iuh.constant;

public class RouterConstant {
    // BASE ADMIN API PATH ====================================
    public static final String ADMIN_REPORT_BASE_PATH = "/admin/v1/reports";
    public static final String ADMIN_PAYMENT_BASE_PATH = "/admin/v1/payments";
    public static final String ADMIN_CALLBACK_BASE_PATH = "/admin/v1/callback";
    public static final String ADMIN_EMPLOYEE_BASE_PATH = "/admin/v1/employees";
    public static final String ADMIN_CINEMA_BASE_PATH = "/admin/v1/cinemas";

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
}

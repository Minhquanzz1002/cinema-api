package vn.edu.iuh.constant;

public class RouterConstant {
    private static final String V1 = "/v1";
    private static final String ADMIN = "/admin";

    // BASE ADMIN API PATH ====================================
    public static final String ADMIN_AUTH_BASE_PATH = "/admin/v1/auth";
    public static final String ADMIN_REPORT_BASE_PATH = "/admin/v1/reports";
    public static final String ADMIN_PAYMENT_BASE_PATH = "/admin/v1/payments";
    public static final String ADMIN_CALLBACK_BASE_PATH = "/admin/v1/callback";
    public static final String ADMIN_EMPLOYEE_BASE_PATH = "/admin/v1/employees";
    public static final String ADMIN_ROOM_BASE_PATH = "/admin/v1/rooms";
    public static final String ADMIN_SHOWTIME_BASE_PATH = "/admin/v1/show-times";
    public static final String ADMIN_MOVIE_BASE_PATH = "/admin/v1/movies";

    // BASE API PATH ==========================================
    public static final String PAYMENT_BASE_PATH = "/v1/payments";

    // ENDPOINT URL ADMIN AUTH ================================
    public static final String POST_ADMIN_LOGIN_SUB_PATH = "/login";

    // ENDPOINT URL ADMIN REPORT ===============================
    public static final String GET_ADMIN_REPORT_DAILY_SUB_PATH = "/daily";
    public static final String GET_ADMIN_REPORT_PROMOTION_SUB_PATH = "/promotion";

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

    // ENDPOINT URL ADMIN MOVIE =============================
    public static final String GET_ADMIN_MOVIE_FOR_SALE_SUB_PATH = "/sales";
    public static final String GET_ADMIN_MOVIE_FOR_FILTER_SUB_PATH = "/filters";
    public static final String GET_ADMIN_MOVIE_SUB_PATH = "/{code}";
    public static final String DELETE_ADMIN_MOVIE_SUB_PATH = "/{id}";
    public static final String PUT_ADMIN_MOVIE_SUB_PATH = "/{id}";

    public static class AdminPaths {
        private static final String ADMIN_V1 = ADMIN + V1;

        public static class Actor {
            public static final String BASE = ADMIN_V1 + "/actors";
            public static final String DETAIL = "/{code}";
            public static final String UPDATE = "/{id}";
            public static final String DELETE = "/{id}";
        }

        public static class Cinema {
            public static final String BASE = ADMIN_V1 + "/cinemas";
            public static final String DETAIL = "/{id}";
            public static final String UPDATE = "/{id}";
            public static final String DELETE = "/{id}";
            public static final String GET_ROOMS = "/{id}/rooms";
        }

        public static class Director {
            public static final String BASE = ADMIN_V1 + "/directors";
        }

        public static class Promotion {
            public static final String BASE = ADMIN_V1 + "/promotions";
            public static final String DETAIL = "/{code}";
            public static final String DELETE = "/{id}";
            public static final String UPDATE = "/{id}";
            public static final String GET_LINES = "/{id}/lines";
            public static final String CREATE_LINES = "/{id}/lines";
        }

        public static class Product {
            public static final String BASE = ADMIN_V1 + "/products";
            public static final String DETAIL = "/{code}";
            public static final String DELETE = "/{code}";
            public static final String UPDATE = "/{code}";
        }

        public static class TicketPrice {
            public static final String BASE = ADMIN_V1 + "/ticket-prices";
            public static final String DETAIL = "/{id}";
            public static final String UPDATE = "/{id}";
            public static final String DELETE = "/{id}";
            public static final String CREATE_LINES = "/{id}/lines";
            public static final String LINE_DETAIL = "/{id}/lines/{lineId}";
        }

        public static class Order {
            public static final String BASE = ADMIN_V1 + "/orders";
            public static final String DETAIL = "/{code}";
            public static final String UPDATE_PRODUCTS = "/{orderId}/products";
            public static final String UPDATE_SEATS = "/{orderId}/seats";
            public static final String COMPLETE = "/{orderId}/complete";
            public static final String CANCEL = "/{orderId}";
            public static final String REFUND = "/{orderId}/refund";
        }
    }
}

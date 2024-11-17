package vn.edu.iuh.constant;

public class RouterConstant {
    private static final String V1 = "/v1";
    private static final String ADMIN = "/admin";

    // BASE ADMIN API PATH ====================================
    public static final String ADMIN_ROOM_BASE_PATH = "/admin/v1/rooms";
    public static final String ADMIN_MOVIE_BASE_PATH = "/admin/v1/movies";

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

        public static class Auth {
            public static final String BASE = ADMIN_V1 + "/auth";
            public static final String PROFILE = "/profile";
            public static final String FORGOT_PASSWORD = "/forgot-password";
            public static final String LOGIN = "/login";
            public static final String LOGOUT = "/logout";
        }

        public static class Callback {
            public static final String BASE = ADMIN_V1 + "/callback";
            public static final String ZALOPAY = "/zalo-pay";
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

        public static class Payment {
            public static final String BASE = ADMIN_V1 + "/payments";
            public static final String CREATE_ORDER_ZALOPAY = "/zalo-pay";
            public static final String STATUS_ORDER_ZALOPAY = "/zalo-pay";
            public static final String DELETE = "/{id}";
            public static final String UPDATE = "/{id}";
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

        public static class ProductPrice {
            public static final String BASE = ADMIN_V1 + "/product-prices";
            public static final String UPDATE = "/{id}";
            public static final String DELETE = "/{id}";
        }

        public static class TicketPrice {
            public static final String BASE = ADMIN_V1 + "/ticket-prices";
            public static final String DETAIL = "/{id}";
            public static final String UPDATE = "/{id}";
            public static final String DELETE = "/{id}";
            public static final String CREATE_LINES = "/{id}/lines";
            public static final String LINE_DETAIL = "/{id}/lines/{lineId}";
        }

        public static class Employee {
            public static final String BASE = ADMIN_V1 + "/employees";
            public static final String DETAIL = "/{code}";
            public static final String UPDATE = "/{id}";
            public static final String DELETE = "/{id}";
        }

        public static class Refund {
            public static final String BASE = ADMIN_V1 + "/refunds";
            public static final String DETAIL = "/{code}";
        }

        public static class Report {
            public static final String BASE = ADMIN_V1 + "/reports";
            public static final String DAILY = "/daily";
            public static final String PROMOTION = "/promotion";
        }

        public static class ShowTime {
            public static final String BASE = ADMIN_V1 + "/show-times";
            public static final String UPDATE = "/{id}";
            public static final String DELETE = "/{id}";
            public static final String SALE = "/sales";
            public static final String FILTER = "/filters";
            public static final String GENERATE = "/generate";
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

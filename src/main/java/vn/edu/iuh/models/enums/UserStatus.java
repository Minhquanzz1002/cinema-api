package vn.edu.iuh.models.enums;

public enum UserStatus {
    ACTIVE,         // Người dùng đang hoạt động bình thường
    INACTIVE,       // Tài khoản tạm thời không hoạt động
    PENDING,        // Đang chờ xác minh hoặc kích hoạt
    SUSPENDED,      // Tài khoản bị tạm ngưng vì vi phạm quy định
    BANNED,         // Tài khoản bị cấm vĩnh viễn
    DELETED         // Tài khoản đã bị xóa
}

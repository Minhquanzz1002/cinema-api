package vn.edu.iuh.projections.admin.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Value;
import vn.edu.iuh.models.enums.AgeRating;
import vn.edu.iuh.models.enums.OrderDetailType;
import vn.edu.iuh.models.enums.OrderStatus;
import vn.edu.iuh.models.enums.RefundStatus;
import vn.edu.iuh.projections.v1.ProductInOrderProjection;
import vn.edu.iuh.projections.v1.SeatInOrderProjection;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface AdminOrderOverviewProjection {
    UUID getId();
    OrderStatus getStatus();
    String getCode();
    float getTotalPrice();
    float getFinalAmount();
    float getTotalDiscount();
    LocalDateTime getOrderDate();
    ShowTime getShowTime();

    List<OrderDetail> getOrderDetails();
    Float getRefundAmount();
    LocalDateTime getRefundDate();
    String getCancelReason();
    RefundStatus getRefundStatus();

    User getUser();

    interface User {
        UUID getId();
        String getName();
        String getEmail();
        String getPhone();
    }

    interface ShowTime {
        UUID getId();
        LocalDate getStartDate();
        LocalTime getStartTime();
        LocalTime getEndTime();

        @Value("#{target.cinema.name}")
        String getCinemaName();
        @Value("#{target.room.name}")
        String getRoomName();
        Movie getMovie();

        interface Movie {
            Integer getId();
            String getTitle();
            String getImagePortrait();
            String getSlug();
            AgeRating getAgeRating();
            Integer getDuration();
        }
    }

    interface OrderDetail {
        Integer getQuantity();
        float getPrice();
        @JsonProperty("isGift")
        boolean isGift();
        OrderDetailType getType();
        ProductInOrderProjection getProduct();
        SeatInOrderProjection getSeat();
    }
}

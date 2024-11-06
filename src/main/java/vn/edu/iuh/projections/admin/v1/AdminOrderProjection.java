package vn.edu.iuh.projections.admin.v1;


import com.fasterxml.jackson.annotation.JsonProperty;
import vn.edu.iuh.models.enums.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface AdminOrderProjection {
    UUID getId();

    String getCode();

    float getTotalPrice();
    float getFinalAmount();
    float getTotalDiscount();
    LocalDateTime getOrderDate();

    OrderStatus getStatus();
    ShowTimeDTO getShowTime();
    List<OrderDetailDTO> getOrderDetails();

    interface ShowTimeDTO {
        UUID getId();
        LocalDate getStartDate();
        LocalTime getStartTime();
        LocalTime getEndTime();
        CinemaDTO getCinema();
        int getTotalSeat();
        int getBookedSeat();
        RoomDTO getRoom();
        MovieDTO getMovie();
    }

    interface RoomDTO {
        Integer getId();
        String getName();
    }

    interface CinemaDTO {
        Integer getId();
        String getName();
    }

    interface MovieDTO {
        Integer getId();

        String getTitle();

        String getImageLandscape();

        String getImagePortrait();

        String getTrailer();

        String getSlug();

        Integer getDuration();

        String getSummary();

        Float getRating();

        String getCountry();
        AgeRating getAgeRating();
        LocalDate getReleaseDate();

        MovieStatus getStatus();
    }

    interface OrderDetailDTO {
        Integer getQuantity();
        float getPrice();
        @JsonProperty("isGift")
        boolean isGift();
        OrderDetailType getType();
        ProductDTO getProduct();
        SeatDTO getSeat();
    }

    interface ProductDTO {
        Integer getId();

        String getName();

        String getDescription();

        String getCode();

        String getImage();

        ProductStatus getStatus();
    }

    interface SeatDTO {
        Integer getId();
        String getName();
        int getArea();
        int getColumnIndex();
        int getRowIndex();
        SeatType getType();
        SeatStatus getStatus();
        String getFullName();

    }
}

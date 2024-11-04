package vn.edu.iuh.dto.res;

import lombok.*;
import vn.edu.iuh.models.enums.SeatStatus;
import vn.edu.iuh.models.enums.SeatType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomLayoutResponseDTO {
    private int id;
    private int maxColumn;
    private int maxRow;
    private List<RowSeatDTO> rows;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RowSeatDTO {
        private int index;
        private String name;
        private List<SeatDTO> seats;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatDTO {
        private int id;
        private short area;
        private short columnIndex;
        private short rowIndex;
        private SeatType type;
        private SeatStatus status;
        private String name;
        private String fullName;
        private boolean booked;
        private Float price;
        private List<GroupSeatDTO> groupSeats;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroupSeatDTO {
        private short area;
        private short rowIndex;
        private short columnIndex;

    }
}

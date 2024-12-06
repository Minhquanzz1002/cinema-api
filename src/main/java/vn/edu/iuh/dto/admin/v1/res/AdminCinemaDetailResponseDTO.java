package vn.edu.iuh.dto.admin.v1.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.iuh.models.enums.BaseStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCinemaDetailResponseDTO {
    private int id;
    private String code;
    private String name;
    private String address;
    private String ward;
    private String wardCode;
    private String district;
    private String districtCode;
    private String city;
    private String cityCode;
    private List<String> images;
    private String hotline;
    private String slug;
    private List<RoomDTO> rooms;
    private BaseStatus status;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    private static class RoomDTO {
        private int id;
        private String name;
        private BaseStatus status;
    }
}

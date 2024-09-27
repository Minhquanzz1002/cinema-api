package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.RoomLayoutResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.RoomLayout;

import java.util.UUID;

public interface RoomLayoutService {
    SuccessResponse<RoomLayoutResponseDTO> getByShowTimeId(UUID showTimeId);
}

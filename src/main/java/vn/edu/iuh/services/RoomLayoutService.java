package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.RoomLayoutResponseDTO;
import vn.edu.iuh.dto.common.SuccessResponse;

import java.util.UUID;

public interface RoomLayoutService {
    SuccessResponse<RoomLayoutResponseDTO> getByShowTimeId(UUID showTimeId);
}

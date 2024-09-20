package vn.edu.iuh.services;

import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.RoomLayout;

public interface RoomLayoutService {
    SuccessResponse<?> getByRoomId(int roomId);
}

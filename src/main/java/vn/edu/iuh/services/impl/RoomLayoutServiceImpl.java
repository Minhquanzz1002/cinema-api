package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.models.Room;
import vn.edu.iuh.models.RoomLayout;
import vn.edu.iuh.projections.RoomLayoutProjection;
import vn.edu.iuh.repositories.RoomLayoutRepository;
import vn.edu.iuh.services.RoomLayoutService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomLayoutServiceImpl implements RoomLayoutService {
    private final RoomLayoutRepository roomLayoutRepository;
    @Override
    public SuccessResponse<?> getByRoomId(int roomId) {
        RoomLayoutProjection layout = roomLayoutRepository.findByRoom(Room.builder().id(roomId).build(), RoomLayoutProjection.class).get();
        return new SuccessResponse<>(200, "success", "Thành công", layout);
    }
}

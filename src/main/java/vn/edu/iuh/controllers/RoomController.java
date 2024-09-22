package vn.edu.iuh.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.RoomLayoutService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/rooms")
@Tag(name = "Room Controller", description = "Quản lý phòng chiếu phim")
public class RoomController {
    private final RoomLayoutService roomLayoutService;

    @Operation(
            summary = "Sơ đồ ghế của phòng chiếu phim"
    )
    @GetMapping("/{roomId}/layout")
    public SuccessResponse<?> getRoomLayoutByRoomId(@PathVariable int roomId) {
        return roomLayoutService.getByRoomId(roomId);
    }
}

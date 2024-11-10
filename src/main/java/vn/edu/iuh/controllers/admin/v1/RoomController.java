package vn.edu.iuh.controllers.admin.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import vn.edu.iuh.dto.admin.v1.req.CreateRoomDTO;
import vn.edu.iuh.dto.admin.v1.req.RoomDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateRoomDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.services.RoomService;

import static vn.edu.iuh.constant.RouterConstant.ADMIN_ROOM_BASE_PATH;

@Slf4j
@RestController("roomControllerAdminV1")
@RequestMapping(ADMIN_ROOM_BASE_PATH)
@RequiredArgsConstructor
@Tag(name = "Room Controller Admin V1", description = "Quản lý phòng chiếu")
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    @Operation(summary = "Get all rooms with pagination and filters")
    @ApiResponse(responseCode = "200", description = "Success")
    public SuccessResponse<Page<RoomDTO>> getRooms(
            @Parameter(description = "Search by name or id")
            @RequestParam(required = false) String search,
            @PageableDefault Pageable pageable
    ) {
        Page<RoomDTO> rooms = roomService.getRooms(search, pageable);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Lấy danh sách phòng chiếu thành công",
                rooms
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get room details by ID")
    @ApiResponse(responseCode = "200", description = "Success")
    public SuccessResponse<RoomDTO> getRoom(
            @Parameter(description = "Room ID")
            @PathVariable int id
    ) {
        RoomDTO room = roomService.getRoomById(id);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Lấy thông tin phòng chiếu thành công",
                room
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create new room")
    @ApiResponse(responseCode = "201", description = "Created successfully")
    public SuccessResponse<RoomDTO> createRoom(
            @Valid @RequestBody CreateRoomDTO dto
    ) {
        RoomDTO room = roomService.createRoom(dto);
        return new SuccessResponse<>(
                HttpStatus.CREATED.value(),
                "success",
                "Thêm phòng chiếu thành công",
                room
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update room information")
    @ApiResponse(responseCode = "200", description = "Updated successfully")
    public SuccessResponse<RoomDTO> updateRoom(
            @Parameter(description = "Room ID")
            @PathVariable int id,
            @Valid @RequestBody UpdateRoomDTO dto
    ) {
        RoomDTO room = roomService.updateRoom(id, dto);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Cập nhật thông tin phòng chiếu thành công",
                room
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete room")
    @ApiResponse(responseCode = "200", description = "Deleted successfully")
    public SuccessResponse<Void> deleteRoom(
            @Parameter(description = "Room ID")
            @PathVariable int id
    ) {
        roomService.deleteRoom(id);
        return new SuccessResponse<>(
                HttpStatus.OK.value(),
                "success",
                "Xóa phòng chiếu thành công",
                null
        );
    }
}
package vn.edu.iuh.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.iuh.dto.admin.v1.req.CreateRoomDTO;
import vn.edu.iuh.dto.admin.v1.req.RoomDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateRoomDTO;

import java.util.List;

public interface RoomService {

    Page<RoomDTO> getRooms(String search, Pageable pageable);


    RoomDTO getRoomById(int id);


    @Transactional
    RoomDTO createRoom(CreateRoomDTO dto);

    @Transactional
    RoomDTO updateRoom(int id, UpdateRoomDTO dto);

    void deleteRoom(int id);

    List<RoomDTO> getRoomsByCinemaId(int cinemaId);
}

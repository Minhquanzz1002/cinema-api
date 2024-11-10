package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.edu.iuh.dto.admin.v1.req.CreateRoomDTO;
import vn.edu.iuh.dto.admin.v1.req.RoomDTO;
import vn.edu.iuh.dto.admin.v1.req.UpdateRoomDTO;
import vn.edu.iuh.models.Cinema;
import vn.edu.iuh.models.Room;
import vn.edu.iuh.repositories.CinemaRepository;
import vn.edu.iuh.repositories.RoomRepository;
import vn.edu.iuh.services.RoomService;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;

    @Override
    public Page<RoomDTO> getRooms(String search, Pageable pageable) {
        try {
            Specification<Room> spec = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                predicates.add(criteriaBuilder.equal(root.get("deleted"), false));

                if (search != null && !search.isEmpty()) {
                    try {
                        int id = Integer.parseInt(search);
                        predicates.add(criteriaBuilder.equal(root.get("id"), id));
                    } catch (NumberFormatException e) {
                        predicates.add(criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                "%" + search.toLowerCase() + "%"
                        ));
                    }
                }

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };

            Page<Room> roomPage = roomRepository.findAll(spec, pageable);

            List<RoomDTO> roomDTOs = roomPage.getContent()
                    .stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(
                    roomDTOs,
                    roomPage.getPageable(),
                    roomPage.getTotalElements()
            );
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while getting rooms: " + e.getMessage());
        }
    }

    @Override
    public RoomDTO getRoomById(int id) {
        Room room = roomRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));
        return toDTO(room);
    }

    @Override
    @Transactional
    public RoomDTO createRoom(CreateRoomDTO dto) {
        Cinema cinema = cinemaRepository.findById(dto.getCinemaId())
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + dto.getCinemaId()));

        Room room = Room.builder()
                .name(dto.getName())
                .cinema(cinema)
                .build();

        return toDTO(roomRepository.save(room));
    }

    @Override
    @Transactional
    public RoomDTO updateRoom(int id, UpdateRoomDTO dto) {
        Room room = roomRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));

        room.setName(dto.getName());

        return toDTO(roomRepository.save(room));
    }

    @Override
    @Transactional
    public void deleteRoom(int id) {
        Room room = roomRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));

        room.setDeleted(true);
        roomRepository.save(room);
    }

    private RoomDTO toDTO(Room room) {
        return RoomDTO.builder()
                .id(room.getId())
                .name(room.getName())
                .cinemaId(room.getCinema().getId())
                .build();
    }
}
package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.edu.iuh.dto.admin.v1.room.req.CreateRoomRequest;
import vn.edu.iuh.dto.admin.v1.req.RoomDTO;
import vn.edu.iuh.dto.admin.v1.room.req.UpdateRoomRequest;
import vn.edu.iuh.models.*;
import vn.edu.iuh.models.enums.SeatType;
import vn.edu.iuh.repositories.*;
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
    private final ModelMapper modelMapper;
    private final RoomLayoutRepository roomLayoutRepository;
    private final RowSeatRepository rowSeatRepository;
    private final SeatRepository seatRepository;
    private final GroupSeatRepository groupSeatRepository;

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
    public RoomDTO createRoom(CreateRoomRequest dto) {
        Cinema cinema = cinemaRepository.findById(dto.getCinemaId())
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + dto.getCinemaId()));

        Room room = Room.builder()
                .name(dto.getName())
                .cinema(cinema)
                .status(dto.getStatus())
                .build();
        room = roomRepository.save(room);

        insertLayout(room);
        return toDTO(room);
    }

    @Override
    @Transactional
    public RoomDTO updateRoom(int id, UpdateRoomRequest dto) {
        Room room = roomRepository.findByIdAndDeleted(id, false)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + id));

        room.setName(dto.getName());
        room.setStatus(dto.getStatus());

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

    @Override
    public List<RoomDTO> getRoomsByCinemaId(int cinemaId) {
        List<Room> rooms = roomRepository.findAllByDeletedAndCinema_Id(false, cinemaId);
        return rooms.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private RoomDTO toDTO(Room room) {
        return RoomDTO.builder()
                .id(room.getId())
                .name(room.getName())
                .cinemaId(room.getCinema().getId())
                .build();
    }

    @Async
    public void insertLayout(Room room) {
        int totalSeat = 21;
        int[][][] layout = {
                {{8}, {7}, {0}, {6, 5}, {5, 6}, {0}, {4}, {3}, {0}, {2, 1}, {1, 2}},
                {},
                {{7, 6}, {6, 7}, {0}, {5}, {0}, {0}, {4, 3}, {3, 4}, {0}, {2}, {1}},
                {},
                {{7}, {0}, {6, 5}, {5, 6}, {0}, {0}, {4}, {3}, {0}, {2, 1}, {1, 2}},
                {},
                {{7, 6}, {6, 7}, {0}, {5}, {0}, {0}, {4, 3}, {3, 4}, {0}, {2}, {1}}
        };

        int maxRow = layout.length;
        int maxColumn = layout[0].length;
        RoomLayout roomLayout = roomLayoutRepository.save(
                RoomLayout.builder()
                          .room(room)
                          .maxRow(maxRow)
                          .maxColumn(maxColumn)
                          .totalSeat(totalSeat)
                          .build()
        );

        RowSeat row;
        Seat seat;
        int count = 0;
        int maxRowData = 0;

        for (int[][] ints : layout) {
            if (ints.length != 0) {
                maxRowData++;
            }
        }

        for (int i = 0; i < maxRow; i++) {
            if (layout[i].length == 0) {
                continue;
            }

            String name = String.valueOf((char) ('A' + maxRowData - 1 - (count++)));

            row = rowSeatRepository.save(
                    RowSeat.builder()
                           .index((short) i)
                           .name(name)
                           .layout(roomLayout)
                           .build()
            );

            for (int j = 0; j < maxColumn; j++) {
                if (layout[i][j][0] == 0) {
                    continue;
                }

                if (layout[i][j].length > 1) {
                    seat = seatRepository.save(
                            Seat.builder()
                                .area((short) layout[i][j].length)
                                .name((short) layout[i][j][0])
                                .fullName(name + layout[i][j][0] + " " + name + layout[i][j][1])
                                .rowIndex((short) i)
                                .columnIndex((short) j)
                                .type(SeatType.COUPLE)
                                .row(row)
                                .build()
                    );

                    groupSeatRepository.save(
                            GroupSeat.builder()
                                     .area((short) 1)
                                     .columnIndex((short) j)
                                     .rowIndex((short) i)
                                     .seat(seat)
                                     .build()
                    );

                    if (layout[i][j][1] > layout[i][j][0]) {
                        groupSeatRepository.save(
                                GroupSeat.builder()
                                         .area((short) 1)
                                         .columnIndex((short) (j - 1))
                                         .rowIndex((short) i)
                                         .seat(seat)
                                         .build()
                        );
                    } else {
                        groupSeatRepository.save(
                                GroupSeat.builder()
                                         .area((short) 1)
                                         .columnIndex((short) (j + 1))
                                         .rowIndex((short) i)
                                         .seat(seat)
                                         .build()
                        );
                    }
                } else {
                    seatRepository.save(
                            Seat.builder()
                                .area((short) layout[i][j].length)
                                .name((short) layout[i][j][0])
                                .fullName(name + layout[i][j][0])
                                .rowIndex((short) i)
                                .columnIndex((short) j)
                                .type(SeatType.NORMAL)
                                .row(row)
                                .build()
                    );
                }
            }
        }
    }
}
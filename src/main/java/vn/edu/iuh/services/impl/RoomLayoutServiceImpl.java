package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.RoomLayoutResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.BadRequestException;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.Seat;
import vn.edu.iuh.models.ShowTime;
import vn.edu.iuh.models.enums.DayType;
import vn.edu.iuh.models.enums.SeatType;
import vn.edu.iuh.projections.v1.*;
import vn.edu.iuh.repositories.RoomLayoutRepository;
import vn.edu.iuh.repositories.SeatRepository;
import vn.edu.iuh.repositories.TicketPriceLineRepository;
import vn.edu.iuh.repositories.TicketPriceRepository;
import vn.edu.iuh.services.RoomLayoutService;
import vn.edu.iuh.services.ShowTimeService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomLayoutServiceImpl implements RoomLayoutService {
    private final RoomLayoutRepository roomLayoutRepository;
    private final TicketPriceLineRepository ticketPriceLineRepository;
    private final TicketPriceRepository ticketPriceRepository;
    private final ShowTimeService showTimeService;
    private final SeatRepository seatRepository;

    @Override
    public SuccessResponse<RoomLayoutResponseDTO> getByShowTimeId(UUID showTimeId) {
        ShowTime showTime = showTimeService.getById(showTimeId);
        List<Seat> bookedSeats = seatRepository.findBookedSeatsByShowTimeAndType(showTime);
        RoomLayoutProjection layout = roomLayoutRepository.findByRoom(showTime.getRoom(), RoomLayoutProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy layout"));
        RoomLayoutResponseDTO roomLayoutResponseDTO = mapToResponseDTO(layout, showTime, bookedSeats);
        return new SuccessResponse<>(200, "success", "Thành công", roomLayoutResponseDTO);
    }

    private RoomLayoutResponseDTO mapToResponseDTO(RoomLayoutProjection layout, ShowTime showTime, List<Seat> bookedSeats) {
        LocalDate startDateShowTime = showTime.getStartDate();
        DayType dayType = convertToDayType(startDateShowTime.getDayOfWeek());

        List<TicketPriceLineProjection> prices = ticketPriceLineRepository.findByDayTypeAndDateAndTimeAndDeleted(dayType.name(), showTime.getStartDate(), showTime.getStartTime(), false);
        log.info("prices: {}", prices);

        if (prices.isEmpty()) {
            throw new BadRequestException("Chưa có giá vé cho ngày này");
        }

        Map<SeatType, Float> priceMap = prices.stream().collect(Collectors.toMap(
                TicketPriceLineProjection::getSeatType,
                TicketPriceLineProjection::getPrice
        ));

        return RoomLayoutResponseDTO.builder()
                .id(layout.getId())
                .maxColumn(layout.getMaxColumn())
                .maxRow(layout.getMaxRow())
                .rows(mapRows(layout.getRows(), priceMap, bookedSeats))
                .build();
    }

    private List<RoomLayoutResponseDTO.RowSeatDTO> mapRows(List<RowSeatProjection> rows, Map<SeatType, Float> priceMap, List<Seat> bookedSeats) {
        return rows.stream()
                .map(row -> RoomLayoutResponseDTO.RowSeatDTO.builder()
                        .index(row.getIndex())
                        .name(row.getName())
                        .seats(mapSeats(row.getSeats(), priceMap, bookedSeats))
                        .build()
                )
                .toList();
    }

    private List<RoomLayoutResponseDTO.SeatDTO> mapSeats(List<SeatProjection> seats, Map<SeatType, Float> priceMap, List<Seat> bookedSeats) {
        return seats.stream()
                .map(seat -> mapSeatToDTO(seat, priceMap, bookedSeats))
                .toList();
    }

    private RoomLayoutResponseDTO.SeatDTO mapSeatToDTO(SeatProjection seat, Map<SeatType, Float> priceMap, List<Seat> bookedSeats) {
        boolean isBooked = bookedSeats.stream().anyMatch(bookedSeat -> bookedSeat.getId() == seat.getId());
        return RoomLayoutResponseDTO.SeatDTO.builder()
                .id(seat.getId())
                .name(seat.getName())
                .fullName(seat.getFullName())
                .area(seat.getArea())
                .status(seat.getStatus())
                .columnIndex(seat.getColumnIndex())
                .rowIndex(seat.getRowIndex())
                .type(seat.getType())
                .booked(isBooked)
                .price(priceMap.getOrDefault(seat.getType(), null))
                .groupSeats(mapGroupSeats(seat.getGroupSeats()))
                .build();
    }

    private List<RoomLayoutResponseDTO.GroupSeatDTO> mapGroupSeats(List<GroupSeatProjection> groupSeats) {
        return groupSeats.stream()
                .map(group -> RoomLayoutResponseDTO.GroupSeatDTO.builder()
                        .area(group.getArea())
                        .columnIndex(group.getColumnIndex())
                        .rowIndex(group.getRowIndex())
                        .build()
                )
                .toList();
    }

    public static DayType convertToDayType(DayOfWeek dayOfWeek) {
        return DayType.valueOf(dayOfWeek.name());
    }
}

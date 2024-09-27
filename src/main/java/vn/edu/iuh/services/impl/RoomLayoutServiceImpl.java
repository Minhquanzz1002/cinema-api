package vn.edu.iuh.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.iuh.dto.res.RoomLayoutResponseDTO;
import vn.edu.iuh.dto.res.SuccessResponse;
import vn.edu.iuh.exceptions.DataNotFoundException;
import vn.edu.iuh.models.ShowTime;
import vn.edu.iuh.models.TicketPrice;
import vn.edu.iuh.models.TicketPriceDetail;
import vn.edu.iuh.models.TicketPriceLine;
import vn.edu.iuh.models.enums.DayType;
import vn.edu.iuh.models.enums.SeatType;
import vn.edu.iuh.projections.v1.*;
import vn.edu.iuh.repositories.RoomLayoutRepository;
import vn.edu.iuh.repositories.TicketPriceLineRepository;
import vn.edu.iuh.services.RoomLayoutService;
import vn.edu.iuh.services.ShowTimeService;
import vn.edu.iuh.services.TicketPriceService;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Collections;
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
    private final ShowTimeService showTimeService;

    @Override
    public SuccessResponse<RoomLayoutResponseDTO> getByShowTimeId(UUID showTimeId) {
        ShowTime showTime = showTimeService.getById(showTimeId);
        RoomLayoutProjection layout = roomLayoutRepository.findByRoom(showTime.getRoom(), RoomLayoutProjection.class).orElseThrow(() -> new DataNotFoundException("Không tìm thấy layout"));
        RoomLayoutResponseDTO roomLayoutResponseDTO = mapToResponseDTO(layout, showTime);
        return new SuccessResponse<>(200, "success", "Thành công", roomLayoutResponseDTO);
    }

    private RoomLayoutResponseDTO mapToResponseDTO(RoomLayoutProjection layout, ShowTime showTime) {
        DayType dayType = convertToDayType(showTime.getStartDate().getDayOfWeek());
        List<TicketPriceLineProjection> prices = ticketPriceLineRepository.findByDayTypeAndDateAndTime(dayType.name(), showTime.getStartDate(), showTime.getStartTime());

//        Map<SeatType, Float> priceMap = createPriceMap(ticketPrice, dayType, showTime.getStartTime());

        Map<SeatType, Float> priceMap = prices.stream().collect(Collectors.toMap(
                TicketPriceLineProjection::getSeatType,
                TicketPriceLineProjection::getPrice
        ));

        return RoomLayoutResponseDTO.builder()
                .id(layout.getId())
                .maxColumn(layout.getMaxColumn())
                .maxRow(layout.getMaxRow())
                .rows(mapRows(layout.getRows(), priceMap))
                .build();
    }

    private List<RoomLayoutResponseDTO.RowSeatDTO> mapRows(List<RowSeatProjection> rows, Map<SeatType, Float> priceMap) {
        return rows.stream()
                .map(row -> RoomLayoutResponseDTO.RowSeatDTO.builder()
                        .index(row.getIndex())
                        .name(row.getName())
                        .seats(mapSeats(row.getSeats(), priceMap))
                        .build()
                )
                .toList();
    }

    private List<RoomLayoutResponseDTO.SeatDTO> mapSeats(List<SeatProjection> seats, Map<SeatType, Float> priceMap) {
        return seats.stream()
                .map(seat -> mapSeatToDTO(seat, priceMap))
                .toList();
    }

    private RoomLayoutResponseDTO.SeatDTO mapSeatToDTO(SeatProjection seat, Map<SeatType, Float> priceMap) {
        return RoomLayoutResponseDTO.SeatDTO.builder()
                .id(seat.getId())
                .name(seat.getName())
                .area(seat.getArea())
                .status(seat.getStatus())
                .columnIndex(seat.getColumnIndex())
                .rowIndex(seat.getRowIndex())
                .type(seat.getType())
                .booked(false)
                .price(priceMap.getOrDefault(seat.getType(), (float) 0))
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

    /**
     * @deprecated
     */
    @Deprecated
    private Map<SeatType, Float> createPriceMap(TicketPrice ticketPrice, DayType dayType, LocalTime showStartTime) {
        return ticketPrice.getTicketPriceLines().stream()
                .filter(line -> line.getApplyForDays().contains(dayType))
                .filter(line -> !showStartTime.isBefore(line.getStartTime()) && !showStartTime.isAfter(line.getEndTime()))
                .findFirst()
                .map(TicketPriceLine::getTicketPriceDetails)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(
                        TicketPriceDetail::getSeatType,
                        TicketPriceDetail::getPrice
                ));

    }

    private DayType convertToDayType(DayOfWeek dayOfWeek) {
        return DayType.valueOf(dayOfWeek.name());
    }
}

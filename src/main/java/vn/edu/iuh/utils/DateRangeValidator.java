package vn.edu.iuh.utils;

import lombok.experimental.UtilityClass;
import vn.edu.iuh.exceptions.BadRequestException;

import java.time.LocalDate;

@UtilityClass
public class DateRangeValidator {
    public static DateRange validateAndGetDateRange(LocalDate fromDate, LocalDate toDate) {
        LocalDate now = LocalDate.now();
        LocalDate effectiveFromDate = fromDate;
        LocalDate effectiveToDate = toDate;

        if (fromDate == null && toDate == null) {
            effectiveFromDate = now.withDayOfMonth(1);
            effectiveToDate = now;
        } else if (fromDate == null) {
            effectiveFromDate = toDate.withDayOfMonth(1);
        } else if (toDate == null) {
            effectiveToDate = now;
        }

        if (effectiveFromDate.isAfter(effectiveToDate)) {
            throw new BadRequestException("Ngày bắt đầu không thể sau ngày kết thúc");
        }

        return new DateRange(effectiveFromDate, effectiveToDate);
    }
}

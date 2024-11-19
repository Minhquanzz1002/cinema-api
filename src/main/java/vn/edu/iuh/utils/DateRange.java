package vn.edu.iuh.utils;

import lombok.Value;

import java.time.LocalDate;

@Value
public class DateRange {
    LocalDate fromDate;
    LocalDate toDate;
}

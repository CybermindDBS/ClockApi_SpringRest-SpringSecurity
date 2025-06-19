package com.example.springrest_refresher.restapi.clockapi.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "Represents Date & Time")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DateTime {
    @Schema(description = "Time zone that this object represents, null if not set")
    private TimeZone timeZone;
    @Schema(example = "10")
    private int hour;
    @Schema(example = "30")
    private int minute;
    @Schema(example = "45")
    private int second;
    @Schema(example = "1")
    private int day;
    @Schema(example = "1")
    private int month;
    @Schema(example = "2025")
    private int year;

    public DateTime(LocalDateTime localDateTime) {
        this.second = localDateTime.getSecond();
        this.minute = localDateTime.getMinute();
        this.hour = localDateTime.getHour();
        this.day = localDateTime.getDayOfMonth();
        this.month = localDateTime.getMonthValue();
        this.year = localDateTime.getYear();
    }

    public void set(LocalDateTime localDateTime) {
        this.second = localDateTime.getSecond();
        this.minute = localDateTime.getMinute();
        this.hour = localDateTime.getHour();
        this.day = localDateTime.getDayOfMonth();
        this.month = localDateTime.getMonthValue();
        this.year = localDateTime.getYear();
    }
}

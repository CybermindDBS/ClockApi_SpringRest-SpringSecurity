package com.example.springrest_refresher.restclient.clockwebapp.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@Schema(description = "Represents Date & Time")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DateTime {
    public LocalDateTime localDateTime;
    @Schema(description = "Time zone that this object represents, null if not set")
    private TimeZone timeZone;
    @Schema(example = "10")
    private Integer hour;
    @Schema(example = "30")
    private Integer minute;
    @Schema(example = "45")
    private Integer second;
    @Schema(example = "1")
    private Integer day;
    @Schema(example = "1")
    private Integer month;
    @Schema(example = "2025")
    private Integer year;

    public DateTime(LocalDateTime localDateTime) {
        this.second = localDateTime.getSecond();
        this.minute = localDateTime.getMinute();
        this.hour = localDateTime.getHour();
        this.day = localDateTime.getDayOfMonth();
        this.month = localDateTime.getMonthValue();
        this.year = localDateTime.getYear();
    }

    public DateTime prepareLocalDateTimeObj() {
        this.localDateTime = LocalDateTime.of(year, month, day, hour, minute, second);
        return this;
    }

    public void set(LocalDateTime localDateTime) {
        this.second = localDateTime.getSecond();
        this.minute = localDateTime.getMinute();
        this.hour = localDateTime.getHour();
        this.day = localDateTime.getDayOfMonth();
        this.month = localDateTime.getMonthValue();
        this.year = localDateTime.getYear();
    }

    public String getDateAsString() {
        return day + "-" + month + "-" + year;
    }

    public String getTimeAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm:ss a");
        return LocalTime.of(hour, minute, second).format(formatter);
    }
}

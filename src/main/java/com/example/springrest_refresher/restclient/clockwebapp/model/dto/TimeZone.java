package com.example.springrest_refresher.restclient.clockwebapp.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class TimeZone {

    @Schema(description = "ID value representing a time zone [https://en.wikipedia.org/wiki/List_of_tz_database_time_zones]", example = "Asia-Kolkata")
    private String timeZoneId;

    public TimeZone(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public static TimeZone of(String timeZoneIdStr) {
        return new TimeZone(timeZoneIdStr);
    }

    public static List<TimeZone> of(List<String> timeZoneIds) {
        return timeZoneIds.stream().map(TimeZone::of).toList();
    }
}

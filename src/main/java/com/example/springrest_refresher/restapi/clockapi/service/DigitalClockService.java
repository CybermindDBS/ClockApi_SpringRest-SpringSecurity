package com.example.springrest_refresher.restapi.clockapi.service;

import com.example.springrest_refresher.restapi.clockapi.model.dto.DateTime;
import com.example.springrest_refresher.restapi.clockapi.model.dto.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DigitalClockService {
    public LocalDateTime dateTime;
    public TimeZone timeZone;
    private LocalDateTime timeSetTimeStamp;
    private LocalDateTime timeSet;

    public DigitalClockService() {
        timeSet = LocalDateTime.now();
        timeSetTimeStamp = LocalDateTime.now();
        this.timeZone = new TimeZone();
        this.timeZone.setTimeZoneId(ZonedDateTime.now().getZone().getId());
    }

    public DateTime getDateTime() {
        dateTime = timeSet.plusNanos(Math.abs(Duration.between(LocalDateTime.now(), timeSetTimeStamp).toNanos()));
        DateTime dateTimeObj = new DateTime(dateTime);
        dateTimeObj.setTimeZone(timeZone);
        return dateTimeObj;
    }

    public void setDateTime(DateTime dateTime) {
        timeSetTimeStamp = LocalDateTime.now();
        this.timeZone = dateTime.getTimeZone();
        this.timeSet = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
        this.dateTime = LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
    }

    public boolean setDateTimeFromTimeZone(TimeZone timeZone) {
        List<DateTime> var = getDateTimeBasedOnTimeZone(List.of(timeZone));
        if (var.get(0) == null) return false;
        DateTime dateTimeObj = var.get(0);
        timeSetTimeStamp = LocalDateTime.now();
        this.timeSet = LocalDateTime.of(dateTimeObj.getYear(), dateTimeObj.getMonth(), dateTimeObj.getDay(), dateTimeObj.getHour(), dateTimeObj.getMinute(), dateTimeObj.getSecond());
        this.dateTime = LocalDateTime.of(dateTimeObj.getYear(), dateTimeObj.getMonth(), dateTimeObj.getDay(), dateTimeObj.getHour(), dateTimeObj.getMinute(), dateTimeObj.getSecond());
        this.timeZone = timeZone;
        return true;
    }

    public void resetDateTime() {
        this.dateTime = LocalDateTime.now();
        this.timeZone.setTimeZoneId(ZonedDateTime.now().getZone().getId());
    }

    public List<DateTime> getDateTimeBasedOnTimeZone(List<TimeZone> timeZones) {
        List<DateTime> dateTimeList = new ArrayList<>();
        for (TimeZone timeZone : timeZones) {
            DateTime datetime = new DateTime();
            try {
                timeZone.setTimeZoneId(timeZone.getTimeZoneId().replace("-", "/"));
                datetime.set(ZonedDateTime.now(ZoneId.of(timeZone.getTimeZoneId())).toLocalDateTime());
                datetime.setTimeZone(timeZone);
                dateTimeList.add(datetime);
            } catch (Exception e) {
                log.warn(e.getMessage());
                dateTimeList.add(null);
            }
        }
        return dateTimeList;
    }
}

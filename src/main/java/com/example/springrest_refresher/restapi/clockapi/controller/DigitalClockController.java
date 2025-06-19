package com.example.springrest_refresher.restapi.clockapi.controller;

import com.example.springrest_refresher.restapi.clockapi.exception.InvalidTimeZoneException;
import com.example.springrest_refresher.restapi.clockapi.model.dto.DateTime;
import com.example.springrest_refresher.restapi.clockapi.model.dto.TimeZone;
import com.example.springrest_refresher.restapi.clockapi.model.response.ApiResponseWrapper;
import com.example.springrest_refresher.restapi.clockapi.service.DigitalClockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*") // controller specific CORS config.
@RequestMapping("/api")
@Tag(name = "DigitalClockApi", description = "Rest Api for Date & Time.")
public class DigitalClockController {

    @Autowired
    DigitalClockService digitalClockService;

    @GetMapping(value = "/get-datetime")
    public ResponseEntity<DateTime> getDateTime() {
        return ResponseEntity.ok().body(digitalClockService.getDateTime());
    }

    @PostMapping("/set-datetime")
    public ResponseEntity<ApiResponseWrapper<String>> setDateTime(@RequestBody DateTime dateTime) {
        digitalClockService.setDateTime(dateTime);
        return ResponseEntity.ok(new ApiResponseWrapper<>(HttpStatus.OK.value(), "success", null));
    }

    @PutMapping("/set-datetime-from-timezone")
    public ResponseEntity<ApiResponseWrapper<String>> setDateTimeFromTimezone(@RequestBody TimeZone timeZone) {
        boolean result = digitalClockService.setDateTimeFromTimeZone(timeZone);
        if (!result) throw new InvalidTimeZoneException("Unknown time-zone ID: " + timeZone.getTimeZoneId());
        return ResponseEntity.ok(new ApiResponseWrapper<>(HttpStatus.OK.value(), "success", null));
    }

    @DeleteMapping("/reset-datetime")
    @Operation(description = "resetDateTime", summary = "Resets the date & time to system time.")
    public ResponseEntity<ApiResponseWrapper<String>> resetDateTime() {
        digitalClockService.resetDateTime();
        return ResponseEntity.ok(new ApiResponseWrapper<>(HttpStatus.OK.value(), "success", null));
    }

    @GetMapping("/datetime-at")
    @Operation(description = "getDateTimeBasedOnTimeZone", summary = "Accepts an array of time zones as input, and outputs the date & time information for all the given time zones.")
    public ResponseEntity<ApiResponseWrapper<List<DateTime>>> getDateTimeBasedOnTimeZone(@RequestParam("timeZone") List<String> timeZoneIds) {

        List<DateTime> dateTimeList = digitalClockService.getDateTimeBasedOnTimeZone(TimeZone.of(timeZoneIds));
        List<String> errors = new ArrayList<>();
        List<DateTime> resultList = new ArrayList<>();


        for (int i = 0; i < dateTimeList.size(); i++) {
            if (dateTimeList.get(i) != null) resultList.add(dateTimeList.get(i));
            else errors.add("Unknown time-zone ID: " + timeZoneIds.get(i));

        }

        ApiResponseWrapper<List<DateTime>> apiResponse = new ApiResponseWrapper<>();
        if (!errors.isEmpty()) {
            apiResponse.setErrors(errors);
            apiResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        } else apiResponse.setStatusCode(HttpStatus.OK.value());
        if (!resultList.isEmpty())
            apiResponse.setResult(resultList);
        if (!errors.isEmpty() && !resultList.isEmpty())
            apiResponse.setStatusCode(HttpStatus.PARTIAL_CONTENT.value());
        return ResponseEntity.status(apiResponse.getStatusCode()).body(apiResponse);
    }

    @GetMapping("/datetime-on/{timeZone}")
    public DateTime getDateTimeBasedOnTimeZone(@PathVariable("timeZone") String timeZoneStr) {

        List<DateTime> timeList = digitalClockService.getDateTimeBasedOnTimeZone(List.of(TimeZone.of(timeZoneStr)));
        if (timeList.get(0) == null)
            throw new InvalidTimeZoneException("Unknown time-zone ID: " + timeZoneStr);

        return timeList.get(0);
    }
}

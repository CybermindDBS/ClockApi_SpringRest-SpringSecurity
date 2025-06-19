package com.example.springrest_refresher.restclient.clockwebapp.service;

import com.example.springrest_refresher.restclient.clockwebapp.model.dto.DateTime;
import com.example.springrest_refresher.restclient.clockwebapp.model.dto.TimeZone;
import com.example.springrest_refresher.restclient.clockwebapp.model.response.ApiResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
public class MyClockService {

    @Autowired
    WebClient webClient;

    private List<TimeZone> subscribedTimeZones;

    public MyClockService() {
        subscribedTimeZones = TimeZone.of(List.of(
                "UTC",
                "America/New_York",
                "America/Los_Angeles",
                "Europe/London",
                "Europe/Paris",
                "Asia/Tokyo",
                "Asia/Kolkata",
                "Australia/Sydney",
                "Europe/Berlin",
                "America/Chicago"
        ));
    }

    public ApiResponseWrapper<?> setDateTime(DateTime dateTime) {
        return webClient
                .post()
                .uri("/set-datetime")
                .bodyValue(dateTime)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponseWrapper<?>>() {
                })
                .doOnSuccess(response -> log.info("setDateTime: " + response.toString()))
                .doOnError(response -> log.error("setDateTime: " + response.toString())).block();
    }

    public void setDateTimeFromTimeZone(TimeZone timeZone) {
        webClient.put().uri("/set-datetime-from-timezone").bodyValue(timeZone).retrieve().bodyToMono(ApiResponseWrapper.class)
                .doOnSuccess(response -> log.info("setDateTimeFromTimeZone: " + response.toString()))
                .doOnError(response -> log.error("setDateTimeFromTimeZone: " + response.toString())).block();
    }

    public DateTime getDateTime() {
        return webClient.get().uri("/get-datetime").retrieve().bodyToMono(DateTime.class).block();
    }

    public List<TimeZone> getSubscribedTimeZones() {
        return this.subscribedTimeZones;
    }

    public void setSubscribedTimeZones(List<TimeZone> timeZones) {
        this.subscribedTimeZones = timeZones;
    }

    public List<DateTime> getSubscribedTimeZoneInformation() {
        return webClient.get().uri((uriBuilder -> {
                    uriBuilder.path("/datetime-at");
                    subscribedTimeZones.forEach(zone -> uriBuilder.queryParam("timeZone", zone.getTimeZoneId()));
                    return uriBuilder.build();
                }))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponseWrapper<List<DateTime>>>() {
                })
                .doOnSuccess(response -> log.info("getSubscribedTimeZoneInformation: " + response.toString()))
                .doOnError(response -> log.error("getSubscribedTimeZoneInformation: " + response.toString()))
                .map(apiResponse -> {
                    if (apiResponse.getErrors() != null)
                        log.error("getSubscribedTimeZoneInformation: " + apiResponse.getErrors());
                    return apiResponse.getResult();
                }).block();
    }
}

package com.example.springrest_refresher.restclient.clockwebapp.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubscribedTimezonesDTO {
    List<String> subscribedTimezonesList;
}

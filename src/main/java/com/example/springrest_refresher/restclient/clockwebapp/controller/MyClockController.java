package com.example.springrest_refresher.restclient.clockwebapp.controller;

import com.example.springrest_refresher.restclient.clockwebapp.model.dto.DateTime;
import com.example.springrest_refresher.restclient.clockwebapp.model.dto.SubscribedTimezonesDTO;
import com.example.springrest_refresher.restclient.clockwebapp.model.dto.TimeZone;
import com.example.springrest_refresher.restclient.clockwebapp.service.MyClockService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/webapp")
public class MyClockController {

    @Autowired
    MyClockService myClockService;

    @GetMapping({"", "/"})
    public String showHomePage(Model model, @RequestParam(required = false) Boolean autoRefresh) {
        DateTime dateTime = myClockService.getDateTime();
        List<DateTime> subscribedTimeZoneDateTimes = myClockService.getSubscribedTimeZoneInformation();

        SubscribedTimezonesDTO subscribedTimezonesDTO = new SubscribedTimezonesDTO();
        subscribedTimezonesDTO.setSubscribedTimezonesList(myClockService.getSubscribedTimeZones().stream().map(TimeZone::getTimeZoneId).toList());
        model.addAttribute("autoRefresh", autoRefresh);
        model.addAttribute("subscribedTimezonesDTO", subscribedTimezonesDTO);
        model.addAttribute("datetime", dateTime);
        model.addAttribute("subscribedTimezonesData", subscribedTimeZoneDateTimes);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = "User";
        if (auth instanceof OAuth2AuthenticationToken) {
            OAuth2User user = ((OAuth2AuthenticationToken) auth).getPrincipal();
            username = (String) user.getAttributes().get("name");
            if (username == null) username = (String) user.getAttributes().get("sub");
        } else if (auth instanceof UsernamePasswordAuthenticationToken)
            username = auth.getName();
        model.addAttribute("username", username);

        return "home";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request, Model model, @RequestParam Optional<String> error) {
        if (error.isPresent()) {
            Exception ex = (Exception) request
                    .getSession()
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

            String msg;
            if (ex instanceof BadCredentialsException) {
                msg = "Username or password is incorrect.";
            } else if (ex instanceof SessionAuthenticationException) {
                msg = "You are already logged in elsewhere.";
            } else {
                msg = ex.getMessage();
            }

            model.addAttribute("errorMessage", msg);
        }

        model.addAttribute("heading", "RestClient | My Clock");
        model.addAttribute("formPostUrl", "/webapp/login");
        model.addAttribute("showGoogleOauth2Login", true);

        return "login";
    }

    @PostMapping("/set-datetime")
    public String setDatetime(@ModelAttribute DateTime dateTime) {
        if (dateTime.localDateTime != null)
            myClockService.setDateTime(new DateTime(dateTime.localDateTime));
        else if (dateTime.getTimeZone() != null && dateTime.getTimeZone().getTimeZoneId() != null && !dateTime.getTimeZone().getTimeZoneId().isEmpty())
            myClockService.setDateTimeFromTimeZone(dateTime.getTimeZone());

        else log.error("Invalid datetime.");
        return "redirect:/webapp?autoRefresh=true";
    }

    @PostMapping("/set-subscribed-timezones")
    public String setSubscribedTimeZones(@ModelAttribute SubscribedTimezonesDTO subscribedTimezonesDTO) {
        myClockService.setSubscribedTimeZones(subscribedTimezonesDTO.getSubscribedTimezonesList().stream().map(TimeZone::of).toList());
        return "redirect:/webapp?autoRefresh=true";
    }
}

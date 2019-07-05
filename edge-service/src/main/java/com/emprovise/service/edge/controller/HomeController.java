package com.emprovise.service.edge.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
class HomeController {

    @GetMapping("/")
    public String echoTheUsersEmailAddress(Principal principal) {
        return "Hey there! Your email address is: " + principal.getName();
    }

    @GetMapping("/stocks")
    @PreAuthorize("hasAnyAuthority('READ_STOCKS')")
    public String getStocks() {
        return "Check out new stocks";
    }

    @GetMapping("/weather")
    @PreAuthorize("canViewWeather()")
    public String getWeather() {
        return "Check out new weather details for ???";
    }
}
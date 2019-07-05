package com.emprovise.service.data.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class DataController {

    @GetMapping("/welcome")
    @PreAuthorize("hasAnyAuthority('READ_STOCKS')")
    @ResponseBody
    public String welcome(@RequestParam(name="name", required=false, defaultValue="Data Fan") String name) {
        return String.format("Hello %s, you are welcome to Data Service!!", name);
    }
}
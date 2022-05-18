package com.denisov.onbalance.activity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("activity")
public class ActivityController {

    @PostMapping("/new")
    public String saveActivity(){
        return "";
    }
}

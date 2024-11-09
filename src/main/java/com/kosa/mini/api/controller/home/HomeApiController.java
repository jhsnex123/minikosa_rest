package com.kosa.mini.api.controller.home;

import com.kosa.mini.api.domain.home.StoreDTO;
import com.kosa.mini.api.service.home.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@PropertySource("classpath:application-key.properties")
public class HomeApiController {
    @Value("${username}")
    private String username;

    @Autowired
    private HomeService service;

    @GetMapping({"/", "/home"})
    public List<StoreDTO> goHome(){
        System.out.println("========================" + username);
        return service.home();
    }
}
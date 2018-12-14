package io.mycred.tron.tronworker.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
    @RequestMapping("/hello")
    public String hello(){
        return "hello_world";
    }
}

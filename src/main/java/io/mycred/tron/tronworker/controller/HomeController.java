package io.mycred.tron.tronworker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/index")
    public String handleError(){
        return "index"; // 这里返回的是文件名，这里会展示 404.html
    }
}

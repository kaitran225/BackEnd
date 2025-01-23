package com.healthy.BackEnd.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedirectController {

    @RequestMapping("/**")
    public RedirectView redirect() {
        return new RedirectView("https://web.com");
    }
} 
package org.gitstats.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }
}

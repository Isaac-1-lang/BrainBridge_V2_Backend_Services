package org.henriette.brainbridge_v2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/")
public class HelloController {
    @GetMapping("/")
    public String HelloController() {
        return "index.html";
    }
}

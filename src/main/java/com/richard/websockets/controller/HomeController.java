package com.richard.websockets.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 11/25/16.
 */
@Controller
public class HomeController {
    private static final String INDEX_FILE_NAME = "index";

    @GetMapping({"", "/"})
    public String index() {
        return INDEX_FILE_NAME;
    }
}

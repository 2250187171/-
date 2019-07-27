package org.java.web;

import org.java.service.PublicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class PublicController {

    @Autowired
    private PublicService service;

}

package org.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class SkipController {

    @RequestMapping("skip/{skip}")
    public String skip(@PathVariable("skip") String skip){
        return "/"+skip;
    }
}

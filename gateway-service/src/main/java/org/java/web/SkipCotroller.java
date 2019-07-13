package org.java.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SkipCotroller {
    @RequestMapping("front/{act}")
    public String req(@PathVariable("act") String act){
        return "front/"+act;
    }
}

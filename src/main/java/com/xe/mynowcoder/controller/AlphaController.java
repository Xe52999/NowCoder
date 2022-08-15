package com.xe.mynowcoder.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xjx
 * @DATE 2022/8/12
 */
@Controller
@RequestMapping("/alpha")
public class AlphaController {

    @GetMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello!";
    }
}

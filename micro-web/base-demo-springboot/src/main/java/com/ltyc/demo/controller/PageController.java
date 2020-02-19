package com.ltyc.demo.controller;

import com.ltyc.common.utils.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author ltyc
 * @version 1.0
 * @Description PageController
 * @create 17.12.18
 */
@Controller
public class PageController {

    @RequestMapping("/")
    public String login(){
        String date = DateUtils.getFormatyyyymmdd();
        System.out.println(date);
        return "index";
    }

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

}

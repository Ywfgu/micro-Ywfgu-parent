package com.ltyc.apple.controller;

import com.ltyc.common.utils.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @author ltyc
 * @version 1.0
 * @Description PageController
 * @create 18.8.23
 */
@RestController
public class TestController {

    @RequestMapping("/test1")
    public String login(){
        String date = DateUtils.getFormatyyyymmdd();
        System.out.println(date);
        return "index";
    }

    @RequestMapping("/test2")
    public String hello(){
        return "/apple/hello";
    }

    public static void main(String[] args) {
        System.out.println("/da.t".lastIndexOf("/"));
        System.out.println("/da.t".substring(0,1));
        System.out.println("-".hashCode());
        System.out.println("/da.t".startsWith("/"));

    }
}

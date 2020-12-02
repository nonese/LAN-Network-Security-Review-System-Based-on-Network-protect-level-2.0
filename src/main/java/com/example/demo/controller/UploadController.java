package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.Util.Getuuid;
import com.example.demo.Util.QEncodeUtil;
import com.example.demo.config.CommonCo;
import com.example.demo.entity.SessionList;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/upload")
public class UploadController {
    @RequestMapping(value = "/image",method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2 = formatter.format(date);
        response.addHeader("Access-Control-Allow-Origin", "*");
        return "";
    }
}

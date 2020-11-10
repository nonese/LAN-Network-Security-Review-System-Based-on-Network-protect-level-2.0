package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Emergency;
import com.example.demo.entity.SystemLog;
import com.example.demo.entity.Tip;
import com.example.demo.service.IEmergencyService;
import com.example.demo.service.ISystemLogService;
import com.example.demo.service.ITipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yaojiaqi
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/tip")
public class TipController {
    @Autowired
    ITipService tipService;
    @Autowired
    ISystemLogService systemLogService;
    SystemLog log =new SystemLog();
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String addtip(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2 = formatter.format(date);
        response.addHeader("Access-Control-Allow-Origin", "*");
        Tip tip = new Tip();
        tip.setContent(request.getParameter("content"));
        tip.setDate(request.getParameter(date2));
        tip.setName(request.getParameter("name"));
        tip.setReaded("unreaded");
        tip.setUuid(request.getParameter("uuid"));
        tipService.save(tip);
        JSONObject result = new JSONObject();
        result.put("statuscode", "200");
        result.put("status", "success");
        log.setRole("info");
        log.setTime(date2);
        log.setContent("用户uuid：" + request.getParameter("uuid") + "发送消息成功！");
        systemLogService.save(log);
        return result.toString();
    }
}

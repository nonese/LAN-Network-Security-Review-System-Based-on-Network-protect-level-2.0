package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Emergency;
import com.example.demo.entity.SystemLog;
import com.example.demo.entity.Tip;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.IEmergencyService;
import com.example.demo.service.ISystemLogService;
import com.example.demo.service.ITipService;
import com.example.demo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    @Autowired
    IUserInfoService userInfoService;
    SystemLog log =new SystemLog();
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String addtip(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2 = formatter.format(date);
        response.addHeader("Access-Control-Allow-Origin", "*");
        QueryWrapper<UserInfo> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("name",request.getParameter("username"));
        List<UserInfo> userInfos=userInfoService.list(sectionQueryWrapper);
        JSONObject result = new JSONObject();
        if (userInfos.isEmpty()==true){
            result.put("status","无叫该名称的用户");
        }
        else {
            for (UserInfo data:userInfos){
                Tip tip = new Tip();
                if (request.getParameter("content").contentEquals("")){
                    result.put("status","空内容，请填写后发送！");
                }
                else {
                    tip.setContent(request.getParameter("content"));
                    tip.setDate(date2);
                    tip.setName(request.getParameter("name"));
                    tip.setReaded("unread");
                    tip.setUuid(data.getUuid());
                    tipService.save(tip);
                    result.put("statuscode", "200");
                    result.put("status", "success");
                    log.setRole("important");
                    log.setTime(date2);
                    log.setContent("用户uuid："+request.getParameter("uuid")+"向名称为："+request.getParameter("username")+"发送消息成功！");
                    systemLogService.save(log);
                }
            }
        }
        return result.toString();
    }
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public String getip(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        QueryWrapper<Tip> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("uuid",request.getParameter("uuid"));
        List<Tip> userInfos=tipService.list(sectionQueryWrapper);
        JSONObject result = new JSONObject();
        JSONArray jsonArray=new JSONArray();
        for (Tip data:userInfos){
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("name",data.getName());
            jsonObject.put("date",data.getDate());
            jsonObject.put("content",data.getContent());
            jsonObject.put("readed",data.getReaded());
            jsonArray.add(jsonObject);
        }
        result.put("data",jsonArray);
        result.put("statuscode","200");
        result.put("status","success");
        return result.toString();
    }
    @RequestMapping(value = "/set",method = RequestMethod.POST)
    public String settip(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        String readed =request.getParameter("readed");
        Tip tip=new Tip();
        tip.setReaded(readed);
        QueryWrapper<Tip> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("uuid",request.getParameter("uuid"));
        sectionQueryWrapper.eq("date",request.getParameter("date"));
        tipService.update(tip,sectionQueryWrapper);
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        result.put("status","success");
        return result.toString();
    }
}

package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Emergency;
import com.example.demo.entity.SystemLog;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.IEmergencyService;
import com.example.demo.service.ISystemLogService;
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
@RequestMapping("/emergency")
public class EmergencyController {
    @Autowired
    IEmergencyService emergencyService;
    @Autowired
    ISystemLogService systemLogService;
    @Autowired
    IUserInfoService userInfoService;
    SystemLog log =new SystemLog();
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String addem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        response.addHeader("Access-Control-Allow-Origin","*");
        Emergency emergency =new Emergency();
        emergency.setContent(request.getParameter("content"));
        emergency.setDate(date2);
        emergency.setName(request.getParameter("name"));
        emergency.setUuid(request.getParameter("uuid"));
        emergencyService.save(emergency);
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        result.put("status","success");
        log.setRole("info");
        log.setTime(date2);
        log.setContent("用户uuid："+request.getParameter("uuid")+"增加紧急事件成功！");
        systemLogService.save(log);
        return result.toString();
    }
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public String getem(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        JSONObject result = new JSONObject();
        JSONArray jsonArray=new JSONArray();
        List<Emergency> datas=emergencyService.list();
        System.out.println(datas.toString());
        for (Emergency data:datas){
            QueryWrapper<UserInfo> userInfoQueryWrapper =new QueryWrapper<>();
            System.out.println();
            userInfoQueryWrapper.eq("uuid",data.getUuid());
            List<UserInfo> userInfos = userInfoService.list(userInfoQueryWrapper);
            for (UserInfo userInfo :userInfos){
                JSONObject js =new JSONObject();
                js.put("date",data.getDate());
                js.put("content",data.getContent());
                js.put("name",data.getName());
                js.put("username",userInfo.getName());
                js.put("uuid",data.getUuid());
                jsonArray.add(js);
            }
        }
        result.put("data",jsonArray);
        result.put("statuscode","200");
        result.put("status","success");
        return result.toString();
    }
}

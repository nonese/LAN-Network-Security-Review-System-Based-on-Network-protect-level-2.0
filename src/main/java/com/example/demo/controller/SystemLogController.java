package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.SystemLog;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.ISystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yaojiaqi
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/system-log")
public class SystemLogController {
    @Autowired
    ISystemLogService systemLogService;
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public String getinfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        QueryWrapper<SystemLog> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("role",request.getParameter("role"));
        List<SystemLog> userInfos=systemLogService.list(sectionQueryWrapper);
        JSONObject result = new JSONObject();
        JSONArray jsonArray=new JSONArray();
        for (SystemLog data:userInfos){
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("tole",data.getRole());
            jsonObject.put("content",data.getContent());
            jsonObject.put("time",data.getTime());
            jsonArray.add(jsonObject);
        }
        result.put("data",jsonArray);
        result.put("statuscode","200");
        result.put("status","success");
        return result.toString();
    }
    @RequestMapping(value = "/getall",method = RequestMethod.POST)
    public String getallinfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        List<SystemLog> userInfos=systemLogService.list();
        JSONArray jsonArray=new JSONArray();
        JSONObject result = new JSONObject();
        for (SystemLog data:userInfos){
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("tole",data.getRole());
            jsonObject.put("content",data.getContent());
            jsonObject.put("time",data.getTime());
            jsonArray.add(jsonObject);
        }
        result.put("data",jsonArray);
        result.put("statuscode","200");
        result.put("status","success");
        return result.toString();
    }
}

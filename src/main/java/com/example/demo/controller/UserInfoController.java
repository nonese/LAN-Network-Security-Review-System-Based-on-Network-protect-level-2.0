package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Device;
import com.example.demo.entity.SystemLog;
import com.example.demo.entity.User;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.IDeviceService;
import com.example.demo.service.ISystemLogService;
import com.example.demo.service.IUserInfoService;
import com.example.demo.service.IUserService;
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
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/user-info")
public class UserInfoController {
    @Autowired
    IUserInfoService userInfoService;
    @Autowired
    IUserService userService;
    @Autowired
    ISystemLogService systemLogService;
    SystemLog log =new SystemLog();
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String addinfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        response.addHeader("Access-Control-Allow-Origin","*");
        UserInfo ui=new UserInfo();
        ui.setEmail(request.getParameter("email"));
        ui.setName(request.getParameter("name"));
        ui.setQqid(request.getParameter("qq"));
        ui.setUuid(request.getParameter("uuid"));
        ui.setWechatsession(request.getParameter("wechat"));
        userInfoService .save(ui);
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        result.put("status","success");
        log.setRole("important");
        log.setTime(date2);
        log.setContent("uuid："+request.getParameter("uuid")+"增加个人信息成功！");
        systemLogService.save(log);
        return result.toString();
    }
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String updateinfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        response.addHeader("Access-Control-Allow-Origin","*");
        UserInfo ui=new UserInfo();
        ui.setEmail(request.getParameter("email"));
        ui.setName(request.getParameter("name"));
        ui.setQqid(request.getParameter("qq"));
        ui.setWechatsession(request.getParameter("wechat"));
        QueryWrapper<UserInfo> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("uuid",request.getParameter("uuid"));
        System.out.println(request.getParameter("uuid"));
        userInfoService .update(ui,sectionQueryWrapper);
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        result.put("status","success");
        log.setRole("important");
        log.setTime(date2);
        log.setContent("uuid："+request.getParameter("uuid")+"修改个人信息成功！");
        systemLogService.save(log);
        return result.toString();
    }
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public String searchinfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        QueryWrapper<UserInfo> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("uuid",request.getParameter("uuid"));
        System.out.println(request.getParameter("uuid"));
        List<UserInfo> userInfos=userInfoService.list(sectionQueryWrapper);
        JSONObject result = new JSONObject();
        for (UserInfo data:userInfos){
            result.put("uuid",request.getParameter("uuid"));
            result.put("email",data.getEmail());
            result.put("name",data.getName());
            result.put("qq",data.getQqid());
            result.put("wechat",data.getWechatsession());
        }
        result.put("statuscode","200");
        result.put("status","success");
        return result.toString();
    }
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    public String delinfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        UserInfo ui=new UserInfo();
        QueryWrapper<UserInfo> sectionQueryWrapper = new QueryWrapper<>();
        QueryWrapper<User> sectionQueryWrapper2 = new QueryWrapper<>();
        sectionQueryWrapper2.eq("uuid",request.getParameter("uuid"));
        sectionQueryWrapper.eq("uuid",request.getParameter("uuid"));
        boolean status = userInfoService.remove(sectionQueryWrapper);
        boolean status2 = userService.remove(sectionQueryWrapper2);
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        result.put("status",status);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        log.setRole("critical");
        log.setTime(date2);
        log.setContent("用户uuid："+request.getParameter("uuid")+"删除账户！");
        return result.toString();
    }
}

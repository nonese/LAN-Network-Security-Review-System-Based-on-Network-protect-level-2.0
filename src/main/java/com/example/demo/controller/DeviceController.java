package com.example.demo.controller;


import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Device;
import com.example.demo.entity.SessionList;
import com.example.demo.entity.SystemLog;
import com.example.demo.service.IDeviceService;
import com.example.demo.service.ISystemLogService;
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
import java.util.UUID;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yaojiaqi
 * @since 2020-08-27
 */
@RestController
@RequestMapping("/device")
public class DeviceController {
    @Autowired
    IDeviceService deviceService;
    @Autowired
    ISystemLogService systemLogService;
    SystemLog log =new SystemLog();
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public String adddevice(HttpServletRequest request, HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        response.addHeader("Access-Control-Allow-Origin","*");
        if (Validator.isIpv4(request.getParameter("ip")) && Validator.isMac(request.getParameter("mac"))){
            Device device=new Device();
            device.setArea(request.getParameter("area"));
            device.setIp(request.getParameter("ip"));
            device.setMac(request.getParameter("mac"));
            device.setName(request.getParameter("name"));
            device.setType(request.getParameter("type"));
            device.setPortlist(request.getParameter("port"));
            device.setUuid(request.getParameter("uuid"));
            device.setStatus("wait");
            deviceService.save(device);
            result.put("statuscode","200");
            result.put("status","success");
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date();
            String date2=formatter.format(date);
            log.setRole("important");
            log.setTime(date2);
            log.setContent("uuid："+request.getParameter("uuid")+"增加个人设备成功！");
            systemLogService.save(log);
            return result.toString();
        }
        else {
            result.put("status","ip地址或mac地址不正确");
            result.put("statuscode","200");
            return result.toString();
        }
    }
    @RequestMapping(value = "/allow",method = RequestMethod.POST)
    public String allowdevice(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        Device device=new Device();
        device.setStatus(request.getParameter("status"));
        QueryWrapper<Device> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("mac",request.getParameter("mac"));
        sectionQueryWrapper.eq("uuid",request.getParameter("uuid"));
        deviceService.update(device,sectionQueryWrapper);
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        result.put("status","success");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        log.setRole("important");
        log.setTime(date2);
        log.setContent("uuid："+request.getParameter("uuid")+"审批个人设备成功！");
        systemLogService.save(log);
        return result.toString();
    }
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    public String deldevice(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        QueryWrapper<Device> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("uuid",request.getParameter("uuid"));
        sectionQueryWrapper.eq("mac",request.getParameter("mac"));
        boolean status = deviceService.remove(sectionQueryWrapper);
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        result.put("status",status);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        log.setRole("critical");
        log.setTime(date2);
        log.setContent("uuid："+request.getParameter("uuid")+"删除个人设备成功！");
        return result.toString();
    }
    @RequestMapping(value = "/listall",method = RequestMethod.POST)
    public String listalldevice(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        JSONObject result = new JSONObject();
        JSONArray ja= new JSONArray();
        List<Device> datas = deviceService.list();
        int count = deviceService.count();
        System.out.println(count);
        System.out.println(datas);
        if (datas.isEmpty()==true){
            result.put("status","false");
        }
        else {
            for (Device data : datas) {
                JSONObject js = new JSONObject();
                js.put("area",data.getArea());
                js.put("name",data.getName());
                js.put("ip",data.getIp());
                js.put("mac",data.getMac());
                js.put("type",data.getType());
                js.put("port",data.getPortlist());
                js.put("uuid",data.getUuid());
                ja.add(js);
            }
        }
        result.put("devicelist",ja);
        result.put("count",count);
        result.put("statuscode","200");
        result.put("status","success");
        return result.toString();
    }
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public String listdevice(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        JSONObject result = new JSONObject();
        JSONArray ja= new JSONArray();
        QueryWrapper<Device> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("uuid",request.getParameter("uuid"));
        List<Device> datas = deviceService.list(sectionQueryWrapper);
        int count = deviceService.count(sectionQueryWrapper);
        System.out.println(count);
        System.out.println(datas);
        if (datas.isEmpty()==true){
            result.put("status","false");
        }
        else {
            for (Device data : datas) {
                JSONObject js = new JSONObject();
                js.put("area",data.getArea());
                js.put("name",data.getName());
                js.put("ip",data.getIp());
                js.put("mac",data.getMac());
                js.put("type",data.getType());
                js.put("port",data.getPortlist());
                js.put("uuid",data.getUuid());
                ja.add(js);
            }
        }
        result.put("devicelist",ja);
        result.put("statuscode","200");
        result.put("count",count);
        result.put("status","success");
        return result.toString();
    }
    @RequestMapping(value = "/list2",method = RequestMethod.POST)
    public String list2device(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        JSONObject result = new JSONObject();
        JSONArray ja= new JSONArray();
        QueryWrapper<Device> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("status",request.getParameter("status"));
        List<Device> datas = deviceService.list(sectionQueryWrapper);
        int count = deviceService.count(sectionQueryWrapper);
        System.out.println(count);
        System.out.println(datas);
        if (datas.isEmpty()==true){
            result.put("status","false");
        }
        else {
            for (Device data : datas) {
                JSONObject js = new JSONObject();
                js.put("area",data.getArea());
                js.put("name",data.getName());
                js.put("ip",data.getIp());
                js.put("mac",data.getMac());
                js.put("type",data.getType());
                js.put("port",data.getPortlist());
                js.put("uuid",data.getUuid());
                ja.add(js);
            }
        }
        result.put("devicelist",ja);
        result.put("statuscode","200");
        result.put("count",count);
        result.put("status","success");
        return result.toString();
    }
}

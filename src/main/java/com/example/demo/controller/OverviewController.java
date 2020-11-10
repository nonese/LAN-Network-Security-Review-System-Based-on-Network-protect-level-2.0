package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.Overview;
import com.example.demo.entity.SystemLog;
import com.example.demo.entity.Tip;
import com.example.demo.service.IOverviewService;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yaojiaqi
 * @since 2020-11-10
 */
@RestController
@RequestMapping("/overview")
public class OverviewController {
    @Autowired
    IOverviewService overviewService;
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public String get(HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        JSONObject result = new JSONObject();
        List<Overview> datas =overviewService.list();
        for (Overview data :datas){
            result.put("totaldevice", data.getTotaldevice());
            result.put("nowdevice", data.getNowdevice());
            result.put("nowtasks", data.getNowtasks());
            result.put("examinetask",data.getExaminetask());
        }
        result.put("statuscode", "200");
        result.put("status", "success");
        return result.toString();
    }

}

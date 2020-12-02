package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Overview;
import com.example.demo.entity.Safereport;
import com.example.demo.service.ISafereportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yaojiaqi
 * @since 2020-12-02
 */
@RestController
@RequestMapping("/safereport")
public class SafereportController {
@Autowired
    ISafereportService safereportService;
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public String get(HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        JSONObject result = new JSONObject();
        QueryWrapper<Safereport> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id","1");
        List<Safereport> datas =safereportService.list(queryWrapper);
        for (Safereport data :datas){
            JSONArray jsonArray=JSONArray.parseArray(data.getContent());
            result.put("data", jsonArray);
        }
        result.put("statuscode", "200");
        result.put("status", "success");
        return result.toString();
    }
}

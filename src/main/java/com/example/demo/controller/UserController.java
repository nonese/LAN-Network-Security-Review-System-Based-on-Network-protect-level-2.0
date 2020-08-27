package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.demo.Util.Getuuid;
import com.example.demo.config.CommonCo;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yaojiaqi
 * @since 2020-08-27
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Autowired
    IUserService userService;
    //@Autowired
    //ISessionListService sessionListService;
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin","*");
        Map<String,Object> columnMap = new HashMap<>();
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        columnMap.put("username",username);
        columnMap.put("password",password);
        Collection user = userService.listByMap(columnMap);
        boolean loginstatus;
        if (user.size() == 1)
            loginstatus =true;
        else
            loginstatus = false;
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        result.put("loginstatus",loginstatus);
        return result.toJSONString();
    }
    @RequestMapping(value = "/AddUser",method = RequestMethod.POST)
    public String addAccount(HttpServletRequest request,HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        Map<String,Object> columnMap = new HashMap<>();
        response.addHeader("Access-Control-Allow-Origin","*");
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String uuid = Getuuid.geuuid();
        columnMap.put("username",username);
        Collection check = userService.listByMap(columnMap);
        if (check.size() == 1) {
            result.put("addstatus","用户名重复");
            result.put("url","https://www.baidu.com");
        }
        else {
           /* User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setUuid(uuid);
            userService.save(user);
            result.put("Username",username);
            result.put("addstatus","success");
            result.put("url","https://www.baidu.com");
            String skey =QEncodeUtil.aesEncrypt(uuid,CommonCo.COOKIE_SECRET_KEY);
            Cookie cookie = new Cookie(CommonCo.SESSION_KEY,skey);
            SessionList session =new SessionList();
            session.setSession(skey);
            session.setTime(date2);
            session.setVaild("true");
            sessionListService.save(session);
            response.addCookie(cookie);*/
        }
        return result.toJSONString();
    }
    @RequestMapping(value = "/Logout",method = RequestMethod.POST)
    public String Logout(HttpServletRequest request,HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        String username=request.getParameter("username");
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        result.put("addstatus","log out success");
        Cookie cookie = new Cookie(CommonCo.COOKIE_KEY,null);
        String session = (String) request.getSession().getAttribute(CommonCo.SESSION_KEY);
        System.out.println(session);
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("session",session);
        //sessionListService.removeByMap(columnMap);
        response.addCookie(cookie);
        return result.toJSONString();
    }
}

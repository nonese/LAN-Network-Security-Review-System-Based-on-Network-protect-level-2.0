package com.example.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Util.Getuuid;
import com.example.demo.Util.LoginUtil;
import com.example.demo.Util.QEncodeUtil;
import com.example.demo.config.CommonCo;
import com.example.demo.entity.SessionList;
import com.example.demo.entity.SystemLog;
import com.example.demo.entity.User;
import com.example.demo.service.ISessionListService;
import com.example.demo.service.ISystemLogService;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    ISessionListService sessionListService;
    @Autowired
    ISystemLogService systemLogService;
    SystemLog log =new SystemLog();
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        response.addHeader("Access-Control-Allow-Origin","*");
        Map<String,Object> columnMap = new HashMap<>();
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        //String aespassword = QEncodeUtil.aesDecrypt(password,CommonCo.REPAIR_SECRET_KEY);;
        columnMap.put("username",username);
        columnMap.put("password",password);
        Collection user = userService.listByMap(columnMap);
        boolean loginstatus;
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        if (user.size() == 1) {
            Object[] arr =user.toArray();
            String message = arr[0].toString().replace("User(","");
            String[] arrays = message.split(",");
            String[] uuidgroup=arrays[0].split("=");
            String uuid =uuidgroup[1];
            loginstatus = true;
            String skey = QEncodeUtil.aesEncrypt(Getuuid.geuuid(),CommonCo.COOKIE_SECRET_KEY);
            Cookie cookie = new Cookie(CommonCo.SESSION_KEY,skey);
            cookie.setMaxAge(60 * 60 * 24 * 7);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            SessionList session =new SessionList();
            session.setSession(skey);
            session.setTime(date2);
            session.setUuid(uuid);
            session.setVaild("true");
            sessionListService.save(session);
            response.addCookie(cookie);
            result.put("session",skey);
            result.put("loginstatus",loginstatus);
            result.put("url","index.html");
            log.setRole("info");
            log.setTime(date2);
            log.setContent("用户："+username+"登陆成功！");
            systemLogService.save(log);
            return result.toJSONString();
        }
        else{
            loginstatus = false;
            result.put("loginstatus",loginstatus);
            return result.toJSONString();
        }
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
           User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setUuid(uuid);
            userService.save(user);
            result.put("Username",username);
            result.put("addstatus","success");
            result.put("url","https://www.baidu.com");
            log.setRole("info");
            log.setTime(date2);
            log.setContent("创建用户："+username+"成功！");
            systemLogService.save(log);
        }
        return result.toJSONString();
    }
    @RequestMapping(value = "/Logout",method = RequestMethod.POST)
    public String Logout(HttpServletRequest request,HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        response.addHeader("Access-Control-Allow-Origin","*");
        String username=request.getParameter("username");
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        Cookie cookie = new Cookie(CommonCo.SESSION_KEY,null);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        String session = LoginUtil.getCookieValue(request,CommonCo.SESSION_KEY);
        if (session=="") {
            result.put("status","you have already log out!");
            return result.toJSONString();
        }
        else {
            result.put("status","log out success");
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("session", session);
            sessionListService.removeByMap(columnMap);
            response.addCookie(cookie);
            log.setRole("info");
            log.setTime(date2);
            log.setContent("用户："+username+"退出成功！");
            systemLogService.save(log);
            return result.toJSONString();
        }
    }
    @RequestMapping(value = "/validate",method = RequestMethod.POST)
    public String validate(HttpServletRequest request,HttpServletResponse response) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        response.addHeader("Access-Control-Allow-Origin","*");
        String sessionid =request.getParameter("sessionid").replace(" ","+");
        System.out.println(sessionid);
        JSONObject result = new JSONObject();
        result.put("statuscode","200");
        QueryWrapper<SessionList> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("session",sessionid);
        List<SessionList> datas = sessionListService.list(sectionQueryWrapper);
        System.out.println(datas);
        if (datas.isEmpty()==true){
            result.put("status","false");
        }
        else {
            for (SessionList data : datas) {
                Date d1=new Date();
                Date d2=formatter.parse(data.getTime());
                boolean checktime=d1.before(d2);
                if (checktime){
                    result.put("status","success");
                }
                else {
                    result.put("status","false");
                }
            }
        }
        //System.out.println(result.toString());
        return result.toString();
    }
}

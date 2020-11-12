package com.example.demo.controller;


import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.lang.Validator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Util.trust;
import com.example.demo.entity.Device;
import com.example.demo.entity.Scan;
import com.example.demo.entity.SystemLog;
import com.example.demo.entity.User;
import com.example.demo.service.IScanService;
import com.example.demo.service.ISystemLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import com.example.demo.Util.nessustool;
import com.example.demo.Util.ParseXml;
import com.example.demo.Util.Runcommand;
/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yaojiaqi
 * @since 2020-11-11
 */
/*
 *
 * 待添加功能：
 * 增加/删除扫描任务（nmap -v -sn 快速扫描网段或者 nmap -T4 -A -v全面扫描某主机端口）✔
 * 增加/删除漏扫任务（nessus 基础全面扫描主机）✔
 * 下载报告（统一传送给文件api到前端服务器进行处理）（xml,jpg,html等）✔
 *
 *
 * */
@RestController
@RequestMapping("/scan")
public class ScanController {
    @Autowired
    IScanService scanService;
    @Autowired
    ISystemLogService systemLogService;
    SystemLog log =new SystemLog();
    nessustool nessustool=new nessustool();
    Runcommand runcommand=new Runcommand();
    @RequestMapping(value = "/addscan",method = RequestMethod.POST)
    public String addscan(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        JSONObject result = new JSONObject();
        String type = request.getParameter("type");
        String uuid =request.getParameter("uuid");
        String ip = request.getParameter("ip");
        String name = request.getParameter("name");
        if (Validator.isIpv4(ip)){
            if (type.contentEquals("quic")){
                Scan scan =new Scan();
                scan.setDate(date2);
                scan.setUuid(uuid);
                scan.setType(type);
                scan.setIp(ip);
                String pid =UUID.randomUUID().toString().replaceAll("-","");
                scan.setPid(pid);
                scan.setStatus("completed");
                scan.setName(name);
                scanService.save(scan);
                String file =runcommand.cmdrunquicnmap2(ip);
                FileReader fileReader =new FileReader(file);
                String readstring = fileReader.readString();
                String uploadfile= nessustool.uploadtxtfile(readstring,pid);
                result.put("filename","assets/file/"+uploadfile);
                result.put("date",date2);
                result.put("pid",pid);
                result.put("status","completed");
                result.put("scanstatus","success");
                result.put("msg","task now is complete");
                result.put("name",name);
                result.put("type",type);
                result.put("ip",ip);
                log.setRole("info");
                log.setTime(date2);
                log.setContent("uuid："+request.getParameter("uuid")+"添加扫描任务成功！");
            }
            else if (type.contentEquals("full"))
            {
                Scan scan =new Scan();
                scan.setDate(date2);
                scan.setUuid(uuid);
                scan.setType(type);
                scan.setIp(ip);
                String pid =UUID.randomUUID().toString().replaceAll("-","");
                scan.setPid(pid);
                scan.setStatus("running");
                scan.setName(name);
                scanService.save(scan);
                Thread t = new Thread(){
                    public void run(){
                        try {
                            String show =runcommand.cmdrunnmap(ip);
                            FileReader fileReader=new FileReader(show);
                            //System.out.println(fileReader.readString());
                            String result =fileReader.readString();
                            nessustool.uploadtxtfile(result,pid);
                            QueryWrapper<Scan> sectionQueryWrapper = new QueryWrapper<>();
                            sectionQueryWrapper.eq("pid",pid);
                            Scan scan1 =new Scan();
                            scan1.setStatus("completed");
                            scanService.update(scan1,sectionQueryWrapper);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
                result.put("scanstatus","success");
                result.put("status","running");
                result.put("date",date2);
                result.put("pid",pid);
                result.put("name",name);
                result.put("type",type);
                result.put("ip",ip);
                result.put("msg","task now is running");
                log.setRole("info");
                log.setTime(date2);
                log.setContent("uuid："+request.getParameter("uuid")+"添加扫描任务成功！");
            }
            else if (type.contentEquals("basic")){
                Scan scan =new Scan();
                scan.setDate(date2);
                scan.setUuid(uuid);
                scan.setType(type);
                scan.setIp(ip);
                String pid =UUID.randomUUID().toString().replaceAll("-","");
                scan.setPid(pid);
                scan.setStatus("running");
                scan.setName(name);
                scanService.save(scan);
                Thread t = new Thread(){
                    public void run(){
                        System.out.println("开始执行基础漏扫");
                        //忽视https错误
                        trust.trustEveryone();
                        //创建session
                        String token = null;
                        try {
                            token = nessustool.createssession();
                            JSONObject jsonObject = JSON.parseObject(nessustool.createscan(name,"no","true",ip,token,"731a8e52-3ea6-a291-ec0a-d2ff0619c19d7bd788d6be818b65"));
                            JSONObject scan =jsonObject.getJSONObject("scan");
                            String id=scan.getString("id");
                            nessustool.scanlaunch(token,id);
                            JSONObject jsonObject2=JSON.parseObject(nessustool.getscandetail(token,id));
                            JSONObject info =jsonObject2.getJSONObject("info");
                            String status = info.getString("status");
                            do {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                JSONObject jsonObject3=JSON.parseObject(nessustool.getscandetail(token,id));
                                JSONObject info2 =jsonObject3.getJSONObject("info");
                                status = info2.getString("status");
                                System.out.println("status:"+status+" ,now waitting!");
                            }while (status.contentEquals("running"));
                            String format="html";
                            JSONObject js =JSON.parseObject(nessustool.creatfile(token, id, format));
                            String fileid=js.getString("file");
                            String token2=js.getString("token");
                            System.out.println(fileid);
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println(nessustool.getfilestatus(token,id,fileid));
                            String content=nessustool.getfile(token,id,fileid);
                            System.out.println(nessustool.uploadfile(content,pid));
                            QueryWrapper<Scan> sectionQueryWrapper = new QueryWrapper<>();
                            sectionQueryWrapper.eq("pid",pid);
                            Scan scan1 =new Scan();
                            scan1.setStatus("completed");
                            scanService.update(scan1,sectionQueryWrapper);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
                result.put("scanstatus","success");
                result.put("status","running");
                result.put("date",date2);
                result.put("pid",pid);
                result.put("msg","task now is running");
                result.put("name",name);
                result.put("type",type);
                result.put("ip",ip);
                log.setRole("important");
                log.setTime(date2);
                log.setContent("uuid："+request.getParameter("uuid")+"添加扫描任务成功！");
            }
            else {
                result.put("scanstatus","false");
                result.put("msg","错误的扫描类型");
            }
        }
        else {
            result.put("scanstatus","false");
            result.put("msg","ip地址不正确");
        }
        return result.toString();
    }
    @RequestMapping(value = "/del",method = RequestMethod.POST)
    public String deldevice(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.addHeader("Access-Control-Allow-Origin","*");
        QueryWrapper<Scan> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("pid",request.getParameter("pid"));
        boolean status = scanService.remove(sectionQueryWrapper);
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
        List<Scan> datas = scanService.list();
        int count = scanService.count();
        System.out.println(count);
        System.out.println(datas);
        if (datas.isEmpty()==true){
            result.put("status","false");
        }
        else {
            for (Scan data : datas) {
                JSONObject js = new JSONObject();
                js.put("name",data.getName());
                js.put("ip",data.getIp());
                js.put("type",data.getType());
                js.put("uuid",data.getUuid());
                js.put("date",data.getDate());
                js.put("status",data.getStatus());
                js.put("pid",data.getPid());
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
        QueryWrapper<Scan> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("uuid",request.getParameter("uuid"));
        List<Scan> datas = scanService.list(sectionQueryWrapper);
        int count = scanService.count(sectionQueryWrapper);
        System.out.println(count);
        System.out.println(datas);
        if (datas.isEmpty()==true){
            result.put("status","false");
        }
        else {
            for (Scan data : datas) {
                JSONObject js = new JSONObject();
                js.put("name",data.getName());
                js.put("ip",data.getIp());
                js.put("type",data.getType());
                js.put("uuid",data.getUuid());
                js.put("date",data.getDate());
                js.put("status",data.getStatus());
                js.put("pid",data.getPid());
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

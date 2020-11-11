package com.example.demo.autotask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Util.ParseXml;
import com.example.demo.Util.Runcommand;
import com.example.demo.entity.Device;
import com.example.demo.entity.Tip;
import com.example.demo.entity.User;
import com.example.demo.service.IDeviceService;
import com.example.demo.service.ITipService;
import com.example.demo.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;
/*
 *
 * 待添加功能：
 * 定时爬虫（360安全报告）（从老代码中复制即可）
 * 定时监控（1小时1次，只扫描服务器，然后根据开放的端口和数据库里的记录进行对比，选出有问题的服务器并向管理员发送消息）
 * 其余待定
 * 可能可以做定期session清理
 *
 * */
@Component
public class Tasks {
    @Autowired
    IDeviceService deviceService;
    @Autowired
    ITipService tipService;
    @Autowired
    IUserService userService;
    @Scheduled(cron = "0 0 * * * ?")
    //@Scheduled(fixedRate = 300000)
    public void scheduledTask() throws IOException, InterruptedException {

        System.out.println("开始日常扫描！");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();
        String date2=formatter.format(date);
        QueryWrapper<Device> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("type","server");
        List<Device> datas = deviceService.list(sectionQueryWrapper);
        Runcommand runcommand=new Runcommand();
        for (Device data :datas){
            String ip =data.getIp();
            String port=data.getPortlist();
            String show =runcommand.cmdrunnmapxml(ip);
            //cn.hutool.core.io.file.FileReader fileReader = new cn.hutool.core.io.file.FileReader(show);
            //System.out.println(fileReader.readString());
            String[] arrays =port.split(",");
            List<String> before2 = new ArrayList<>();
            for (int i=0;i<arrays.length;i++){
                String data2 =arrays[i];
                before2.add(data2);
            }
            ParseXml parseXml =new ParseXml();
            List<String> result1 = parseXml.Nmapxmlparse(show);
            List<String> result2 = new ArrayList<>();
            for (String data2:result1){
                if (before2.contains(data2)){
                }
                else {
                    result2.add(data2);
                    System.out.println("出现额外端口："+data2);
                }
            }
            QueryWrapper<User> sectionQueryWrapper2 = new QueryWrapper<>();
            sectionQueryWrapper2.eq("role","admin");
            List<User> datas2 = userService.list(sectionQueryWrapper2);
            for (User data2:datas2){
                String uuid = data2.getUuid();
                String name ="系统警告";
                String date3 =date2;
                String content ="出现未在登记列表的端口！"+result2.toString();
                String readed ="unread";
                Tip tip =new Tip();
                tip.setUuid(uuid);
                tip.setReaded(readed);
                tip.setDate(date3);
                tip.setContent(content);
                tip.setName(name);
                tipService.save(tip);
            }
        }
    }
}

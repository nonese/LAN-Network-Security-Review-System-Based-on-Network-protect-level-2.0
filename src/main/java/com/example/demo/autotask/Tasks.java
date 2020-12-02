package com.example.demo.autotask;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.Util.ParseXml;
import com.example.demo.Util.Runcommand;
import com.example.demo.entity.*;
import com.example.demo.service.*;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Thread.sleep;
/**
 *
 * 待添加功能：
 * 定时爬虫（360安全报告）（从以前自己写的项目的代码中复制即可）
 * 定时监控（1小时1次，只扫描服务器，然后根据开放的端口和数据库里的记录进行对比，选出有问题的服务器并向管理员发送消息） 已完成！
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
    @Autowired
    IScanService scanService;
    @Autowired
    IOverviewService overviewService;
    @Autowired
    ISafereportService safereportService;
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
        System.out.println("结束日常扫描！");
    }
    @Scheduled(cron = "0 0,30 * * * ?")
    /**
     *
     * 此任务用于日常清点在线设备，总设备数等汇报并保存至数据库
     *
     */
    public void scheduledTask2() throws IOException, InterruptedException {
        System.out.println("日常清点开始");
        int countdevice =deviceService.count();
        Runcommand runcommand =new Runcommand();
        String filepath= runcommand.cmdrunquicnmap("192.168.2.0/24");
        ParseXml parseXml =new ParseXml();
        String onlinedevice = parseXml.Nmapquicxmlparse(filepath);
        System .out.println(onlinedevice);
        QueryWrapper<Scan> sectionQueryWrapper = new QueryWrapper<>();
        sectionQueryWrapper.eq("status","running");
        int countscan =scanService.count(sectionQueryWrapper);
        QueryWrapper<Device> sectionQueryWrapper2 = new QueryWrapper<>();
        sectionQueryWrapper2.eq("status","wait");
        int devicewait=deviceService.count(sectionQueryWrapper2);
        List<Overview> datas =overviewService.list();
        for (Overview data:datas){
            QueryWrapper<Overview> sectionQueryWrapper3 = new QueryWrapper<>();
            sectionQueryWrapper3.eq("totaldevice",data.getTotaldevice());
            overviewService.remove(sectionQueryWrapper3);
        }
        Overview overview =new Overview();
        overview.setExaminetask(String.valueOf(devicewait));
        overview.setNowdevice(onlinedevice);
        overview.setNowtasks(String.valueOf(countscan));
        overview.setTotaldevice(String.valueOf(countdevice));
        overviewService.save(overview);
        System.out.println("日常清点结束");
    }
    @Scheduled(cron = "0 0 7 * * ? ")
    //@Scheduled(fixedRate = 3000000)
    public void scheduledTask3() throws IOException {
        System.out.println("开始执行360安全周报，"+"任务执行时间：" + LocalDateTime.now());
        WebClient browser = new WebClient();
        browser.getOptions().setCssEnabled(false);
        browser.getOptions().setJavaScriptEnabled(true);
        browser.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage htmlPage = browser.getPage("https://cert.360.cn/warning");
        browser.waitForBackgroundJavaScript(1000);
        Document doc = Jsoup.parse(htmlPage.asXml());
        Elements content = doc.getElementsByClass("news-title");
        String[] urlall = new String[5];
        JSONArray jsonArray=new JSONArray();
        for (Element link : content) {
            String urlb = link.select("a").attr("href");
            String title = link.select("a").text();
            String url = "https://cert.360.cn"+urlb;
            JSONObject jsonObject =new JSONObject();
            jsonObject.put("title",title);
            jsonObject.put("url",url);
            jsonArray.add(jsonObject);
        }
        Map<String,Object> columnMap = new HashMap<>();
        columnMap.put("id","1");
        safereportService.removeByMap(columnMap);
        Safereport sf =new Safereport();
        sf.setId("1");
        sf.setContent(jsonArray.toString());
        safereportService.save(sf);
        System.out.println("360安全周报"+"任务执行时间：" + LocalDateTime.now());
    }
}

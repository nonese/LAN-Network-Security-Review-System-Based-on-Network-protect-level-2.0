package com.example.demo.Util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.file.FileWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ParseXml {
    public List<String> Nmapxmlparse(String path) throws IOException {
        Long timeStamp = System.currentTimeMillis();
        String time =timeStamp.toString();
        System.out.println(path);
        File file = new File(path);
        List<String> portlist = new ArrayList<>();
        Document document = Jsoup.parse(file, "utf-8");
        Elements elements = document.getElementsByTag("address");
        Elements Portelements = document.getElementsByTag("port");
        for (Element link : elements) {
            String addrtype =link.attr("addrtype").toString();
            String addr =link.attr("addr").toString();
            System.out.println(addrtype+"："+addr);
        }
        for (Element link : Portelements) {
            String protocol =link.attr("protocol").toString();
            String portid = link.attr("portid").toString();
            portlist.add(portid);
            String state = null;
            String name = null;
            Elements states = link.getElementsByTag("state");
            for(Element link2 :states){
                state =link2.attr("state").toString();
            }
            Elements services = link.getElementsByTag("service");
            for (Element link3 :services){
                name = link3.attr("name").toString();
            }
            System.out.println("protocol:"+protocol+" port:"+portid+" state:"+state+" name:"+name);
        }
        return portlist;
    }
    public String Nmapquicxmlparse(String path) throws IOException {
        System.out.println(path);
        File file = new File(path);
        Document document = Jsoup.parse(file, "utf-8");
        Elements elements = document.getElementsByTag("runstats");
        for (Element link : elements) {
            Elements hostelements = document.getElementsByTag("hosts");
            for (Element link2 : hostelements){
                String up = link2.attr("up");
                System.out.println(up);
                return up;
            }
        }
        return "false";
    }
}

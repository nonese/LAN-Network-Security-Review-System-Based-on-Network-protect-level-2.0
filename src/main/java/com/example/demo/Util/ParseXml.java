package com.example.demo.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

public class ParseXml {
    public void Nmapxmlparse(String path) throws IOException {
        System.out.println(path);
        File file = new File(path);
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
    }
}

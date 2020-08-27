package com.example.demo.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class wechatsend {
    private final Logger logger = LoggerFactory.getLogger(com.example.demo.Util.wechatsend.class);

    public static String postmsg(String title,String content) throws IOException {
        String response=null;
        response = Jsoup.connect("https://sc.ftqq.com/SCU107396T97bea2dd35865cb7b56090d989ca564c5f1f0477553ab.send")
                .method(Connection.Method.POST)
                .data("text",title)
                .data("desp",content)
                .ignoreContentType(true)
                .execute()
                .body();
        JSONObject json = JSON.parseObject(response);
        String msg = json.getString("errmsg");
        System.out.println("\n"+"微信留言"+msg);
        return "发送成功";
    }
}

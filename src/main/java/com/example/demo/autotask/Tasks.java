package com.example.demo.autotask;

import org.springframework.stereotype.Component;

import java.io.IOException;

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
    //@Scheduled(fixedRate = 30000)
    public void scheduledTask() throws IOException, InterruptedException {

    }
}

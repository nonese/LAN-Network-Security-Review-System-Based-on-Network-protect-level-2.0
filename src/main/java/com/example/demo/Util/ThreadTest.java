package com.example.demo.Util;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Test;

public class ThreadTest {
    public static void main(String args[]){
        System.out.println("正常执行。。。111");
        Thread t = new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //2秒执行
                System.out.println("2秒后 执行");
            }
        };
        t.start();
        System.out.println("正常执行。。。222");
    }
}

package com.example.demo.Util;

import cn.hutool.core.collection.ListUtil;
import org.junit.Test;
import com.example.demo.Util.ParseXml;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Runcommand {
    public String cmdrunnmap(String ip) throws IOException {
        Long timeStamp = System.currentTimeMillis();
        String time =timeStamp.toString();
        String file ="C:\\temp\\"+time+".txt";
        String path = "nmap -T4 -A -v "+ip+" -oN "+file;
        System.out.println(path);
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec("cmd.exe /c " + path);
            InputStream in = process.getInputStream();
            while (in.read() != -1) {
                System.out.println(in.read());
            }
            in.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
    public String cmdrunnmapxml(String ip) throws IOException {
        Long timeStamp = System.currentTimeMillis();
        String time =timeStamp.toString();
        String file ="C:\\temp\\"+time+".xml";
        String path = "nmap -T4 -A -v "+ip+" -oX "+file;
        System.out.println(path);
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec("cmd.exe /c " + path);
            InputStream in = process.getInputStream();
            while (in.read() != -1) {
                System.out.println(in.read());
            }
            in.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
    public String cmdrunquicnmap(String ip) throws IOException {
        Long timeStamp = System.currentTimeMillis();
        String time =timeStamp.toString();
        String file ="C:\\temp\\"+time+".txt";
        String path = "nmap -v -sn "+ip+" -oN "+file;
        System.out.println(path);
        Runtime run = Runtime.getRuntime();
        try {
            Process process = run.exec("cmd.exe /c " + path);
            InputStream in = process.getInputStream();
            while (in.read() != -1) {
                System.out.println(in.read());
            }
            in.close();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }
    public String cmdruntime(String command) {
        // CMD 执行命令
        String cmd = "cmd /c "+command;

        try {
            // 执行 CMD 命令
            Long timeStamp = System.currentTimeMillis();
            String time =timeStamp.toString();
            Process process = Runtime.getRuntime().exec(cmd);
            // 从输入流中读取文本
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            // 构造一个写出流并指定输出文件保存路径
            String file ="C:\\temp\\CmdInfo_"+time+".txt";
            FileWriter fw = new FileWriter(new File(file));
            String line = null;
            // 循环读取
            while ((line = reader.readLine()) != null) {
                // 循环写入
                fw.write(line + "\n");
            }
            // 刷新输出流
            fw.flush();
            // 关闭输出流
            fw.close();
            // 关闭输出流
            process.getOutputStream().close();
            System.out.println("Cmd执行完毕!");
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return "执行失败";
        }
    }

    @Test
    public void testcmd() throws IOException {
        //String show =cmdruntime("ping 127.0.0.1");
        //String show = cmdrunquicnmap("192.168.2.0/24");
        String show =cmdrunnmapxml("192.168.2.220");
        //cn.hutool.core.io.file.FileReader fileReader = new cn.hutool.core.io.file.FileReader(show);
        //System.out.println(fileReader.readString());
        String before ="135,139,445,3389";
        String[] arrays =before.split(",");
        List<String> before2 = new ArrayList<>();
        for (int i=0;i<arrays.length;i++){
            String data =arrays[i];
            before2.add(data);
        }
        ParseXml parseXml =new ParseXml();
       List<String> result1 = parseXml.Nmapxmlparse(show);
        List<String> result2 = new ArrayList<>();
       for (String data:result1){
           if (before2.contains(data)){
           }
           else {
               result2.add(data);
               System.out.println("出现额外端口："+data);
           }
       }
       System.out.println(result2.toString());
    }
}

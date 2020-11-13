package com.example.demo.Util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import com.example.demo.Util.trust;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

public class nessustool {
    private final String url = "https://192.168.2.21:8834/";
    private final String fileurl = "http://192.168.2.184:8080/";
    private final String accesskey = "b4f014fbcac433d1b06f4f1c56a235a4018f5309c46a82a9cb66f33bd8e78f46";
    private final String screctkey = "cf70c07dcc641b0cf9f9a0b109383f5a8661feaa934f3d99eb8b32da1699de64";
    private final String username="shfxxbz";
    private final String password="shxb1007";
    public String createssession() throws IOException {
        String response=null;
        response = Jsoup.connect(url+"session")
                .method(Connection.Method.POST)
                //.header("Content-Type","application/json")
                .data("username",username)
                .data("password",password)
                .ignoreContentType(true)
                //.ignoreHttpErrors(true)
                .execute()
                .body();
        JSONObject json = JSON.parseObject(response);
        //System.out.println(json);
        return json.getString("token");
    }
    public String getscantemplates(String token) throws IOException {
        String response=null;
        response = Jsoup.connect(url+"editor/scan/templates")
                .method(Connection.Method.GET)
                .header("X-Cookie","token="+token)
                .header("X-API-Token","1569A6B9-FBFC-44A0-951C-0541A0D9BD82")
                .ignoreContentType(true)
                //.ignoreHttpErrors(true)
                .execute()
                .body();
        JSONObject json = JSON.parseObject(response);
        return json.toString();
    }
    public String getscanlist(String token) throws IOException {
        String response=null;
        response = Jsoup.connect(url+"scans?folder_id=3")
                .method(Connection.Method.GET)
                .header("X-Cookie","token="+token)
                .header("X-API-Token","1569A6B9-FBFC-44A0-951C-0541A0D9BD82")
                .ignoreContentType(true)
                //.ignoreHttpErrors(true)
                .execute()
                .body();
        JSONObject json = JSON.parseObject(response);
        return json.toString();
    }
    public String getscandetail(String token,String id) throws IOException {
        String response=null;
        response = Jsoup.connect(url+"scans/"+id)
                .method(Connection.Method.GET)
                .header("X-Cookie","token="+token)
                .header("X-API-Token","1569A6B9-FBFC-44A0-951C-0541A0D9BD82")
                .ignoreContentType(true)
                //.ignoreHttpErrors(true)
                .execute()
                .body();
        JSONObject json = JSON.parseObject(response);
        return json.toString();
    }
    public void scanlaunch(String token,String id) throws IOException {
        String response=null;
        response = Jsoup.connect(url+"scans/"+id+"/launch")
                .method(Connection.Method.POST)
                .header("X-Cookie","token="+token)
                .header("X-API-Token","1569A6B9-FBFC-44A0-951C-0541A0D9BD82")
                .ignoreContentType(true)
                //.ignoreHttpErrors(true)
                .execute()
                .body();
    }
    public void scanstop(String token,String id) throws IOException {
        String response=null;
        response = Jsoup.connect(url+"scans/"+id+"/stop")
                .method(Connection.Method.POST)
                .header("X-Cookie","token="+token)
                .header("X-API-Token","1569A6B9-FBFC-44A0-951C-0541A0D9BD82")
                .ignoreContentType(true)
                //.ignoreHttpErrors(true)
                .execute()
                .body();
    }
    public String createscan(String name,String description,String enabled,String targets,String token,String typeuuid) throws IOException {
        String uuid = typeuuid;
        JSONObject rs=new JSONObject();
        JSONObject settings=new JSONObject();
        rs.put("uuid",uuid);
        rs.put("settings",settings);
        settings.put("agent_group_id","[]");
        settings.put("name",name);
        settings.put("description",description);
        settings.put("enabled",enabled);
        settings.put("text_targets",targets);
        String response=null;
        response = Jsoup.connect(url+"scans")
                .method(Connection.Method.POST)
                //.header("X-ApiKeys","accessKey={"+accesskey+"}; secretKey={"+screctkey+"};")
                .header("X-Cookie","token="+token)
                .header("X-API-Token","1569A6B9-FBFC-44A0-951C-0541A0D9BD82")
                .header("Content-Type","application/json")
                .requestBody(rs.toString())
                .ignoreContentType(true)
                //.ignoreHttpErrors(true)
                .execute()
                .body();
        JSONObject json = JSON.parseObject(response);
        //System.out.println(json);
        return json.toString();
    }
    public String creatfile(String token,String id,String format) throws IOException {
        String response=null;
        response = Jsoup.connect(url+"scans/"+id+"/export")
                .method(Connection.Method.POST)
                //.header("X-ApiKeys","accessKey={"+accesskey+"}; secretKey={"+screctkey+"};")
                .header("X-Cookie","token="+token)
                .header("X-API-Token","1569A6B9-FBFC-44A0-951C-0541A0D9BD82")
                //.header("Content-Type","application/json")
                .data("scan_id",id)
                .data("format",format)
                .data("chapters","custom;vuln_by_host;vulnerabilities")
                .data("reportContents.formattingOptions.page_breaks","true")
                .data("reportContents.hostSections.scan_information","true")
                .data("reportContents.hostSections.host_information","true")
                .data("reportContents.vulnerabilitySections.description","true")
                .data("reportContents.vulnerabilitySections.risk_factor","true")
                .data("reportContents.vulnerabilitySections.cvss3_base_score","true")
                .data("reportContents.vulnerabilitySections.cvss3_temporal_score","true")
                .data("reportContents.vulnerabilitySections.stig_severity","true")
                .data("reportContents.vulnerabilitySections.references","true")
                .data("reportContents.vulnerabilitySections.exploitable_with","true")
                .data("reportContents.vulnerabilitySections.risk_factor","true")
                .data("reportContents.vulnerabilitySections.solution","true")
                .data("reportContents.vulnerabilitySections.see_also","true")
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute()
                .body();
        JSONObject json = JSON.parseObject(response);
        //System.out.println(json);
        return json.toString();
    }
    public String getfilestatus(String token,String id,String fileid) throws IOException {
        String response=null;
        response = Jsoup.connect(url+"scans/"+id+"/export/"+fileid+"/status")
                .method(Connection.Method.GET)
                //.header("X-ApiKeys","accessKey={"+accesskey+"}; secretKey={"+screctkey+"};")
                .header("X-Cookie","token="+token)
                .header("X-API-Token","1569A6B9-FBFC-44A0-951C-0541A0D9BD82")
                //.header("Content-Type","application/json")
                .data("scan_id",id)
                .data("file_id",fileid)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute()
                .body();
        JSONObject json = JSON.parseObject(response);
        //System.out.println(json);
        return json.toString();
    }
    public String getfile(String token,String id,String fileid) throws IOException {
        String response=null;
        response = Jsoup.connect(url+"scans/"+id+"/export/"+fileid+"/download")
                .method(Connection.Method.GET)
                //.header("X-ApiKeys","accessKey={"+accesskey+"}; secretKey={"+screctkey+"};")
                .header("X-Cookie","token="+token)
                .header("X-API-Token","1569A6B9-FBFC-44A0-951C-0541A0D9BD82")
                //.header("Content-Type","application/json")
                .data("scan_id",id)
                .data("file_id",fileid)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute()
                .body();
        return response.toString();
    }
    public JSONObject showtemplate(JSONObject json){
        JSONArray ja =json.getJSONArray("templates");
        JSONArray jsonArray=new JSONArray();
        for (int i=0;i<ja.size();i++){
            JSONObject result =new JSONObject();
            JSONObject js=ja.getJSONObject(i);
            result.put("category",js.getString("category"));
            result.put("uuid",js.getString("uuid"));
            result.put("title",js.getString("title"));
            jsonArray.add(result);
        }
        JSONObject result2 =new JSONObject();
        result2.put("result",jsonArray);
        //System.out.println(result2.toString());
        return result2;
    }
    public JSONObject showlist(JSONObject json){
        JSONArray ja =json.getJSONArray("scans");
        JSONArray jsonArray=new JSONArray();
        for (int i=0;i<ja.size();i++){
            JSONObject result =new JSONObject();
            JSONObject js=ja.getJSONObject(i);
            result.put("name",js.getString("name"));
            result.put("id",js.getString("id"));
            result.put("uuid",js.getString("uuid"));
            result.put("status",js.getString("status"));
            jsonArray.add(result);
        }
        JSONObject result2 =new JSONObject();
        result2.put("result",jsonArray);
        //System.out.println(result2.toString());
        return result2;
    }
    public void writefile(String filename,String filecontent) throws IOException {
        File writename = new File(getUploadPath()+filename);
        writename.createNewFile();
        FileWriter writer = new FileWriter(writename , true);
        BufferedWriter out = new BufferedWriter(writer);
        out.write(filecontent);
        out.flush();
        out.close();
    }
    public String uploadfile(String filecontent,String filename) throws IOException {
        String response=null;
        response = Jsoup.connect(fileurl+"file/html")
                .method(Connection.Method.POST)
                .data("content",filecontent)
                .data("uuid",filename)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute()
                .body();
        return response.toString();
    }
    public String uploadtxtfile(String filecontent,String filename) throws IOException {
        String response=null;
        response = Jsoup.connect(fileurl+"file/txt")
                .method(Connection.Method.POST)
                .data("content",filecontent)
                .data("uuid",filename)
                .ignoreContentType(true)
                .ignoreHttpErrors(true)
                .execute()
                .body();
        return response.toString();
    }
    private String getUploadPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return "D:/lubo/html/";
        } else {
            return "/home/shfxxbz/download/html/";
        }
    }
    @Test
    public void test() throws IOException, InterruptedException {
        //忽视https错误
        trust.trustEveryone();

        //创建session
        String token =createssession();

        //创建扫描
        //System.out.println(createscan("test2","no","true","192.168.2.220",token,"731a8e52-3ea6-a291-ec0a-d2ff0619c19d7bd788d6be818b65"));
        JSONObject jsonObject =JSON.parseObject(createscan("test5","no","true","192.168.2.220",token,"731a8e52-3ea6-a291-ec0a-d2ff0619c19d7bd788d6be818b65"));
        JSONObject scan =jsonObject.getJSONObject("scan");
        String id=scan.getString("id");
        System.out.println(id);

        //获取扫描模板
        //JSONObject json = JSON.parseObject(getscantemplates(token));
        //System.out.println(showtemplate(json));

        //获取扫描清单
        //System.out.println(getscanlist(token));
        //JSONObject json = JSON.parseObject(getscanlist(token));
        //System.out.println(showlist(json));
        //scanlaunch(token,id);

        //获取扫描细节
        //System.out.println(getscandetail(token,"23"));
        /*JSONObject jsonObject2=JSON.parseObject(getscandetail(token,id));
        JSONObject info =jsonObject2.getJSONObject("info");
        String status = info.getString("status");
        System.out.println(status);
        do {
            Thread.sleep(3000);
            JSONObject jsonObject3=JSON.parseObject(getscandetail(token,id));
            JSONObject info2 =jsonObject3.getJSONObject("info");
            status = info2.getString("status");
            System.out.println("status:"+status+" ,now waitting!");
        }while (status.contentEquals("running"));
           */
        //开启扫描
        scanlaunch(token,id);
        //结束扫描
        //scanstop(token,"14");

        //创建下载文件
        String format="html";
        JSONObject js =JSON.parseObject(creatfile(token, "29", format));
        String fileid=js.getString("file");
        String token2=js.getString("token");
        System.out.println(fileid);
        //url+"/tokens/"+token+"/download"

        //隔3秒获取文件状态
        Thread.sleep(3000);
        System.out.println(getfilestatus(token,"29",fileid));
        //System.out.println(getfile(token,"14",fileid));

        //写入文件
        String content=getfile(token,"29",fileid);
        System.out.println(content);
        //System.out.println(uploadfile(content));
        //System.out.println(content);
        //System.out.println(uploadfile(content));
        //writefile(UUID.randomUUID().toString().replaceAll("-", "")+"."+format,getfile(token,"14",fileid));
        //{"status":"ready"}
    }
}

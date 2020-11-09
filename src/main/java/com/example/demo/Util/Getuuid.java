package com.example.demo.Util;
import org.junit.Test;

import java.util.UUID;
public class Getuuid {

    public static String geuuid(){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        return uuid;
    }
    public static String getUUID2() {
        UUID uuid = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        String str = uuid.toString();
        String str2 = uuid2.toString();
        // 去掉"-"符号
        String temp = str2.substring(0, 8)+ str2.substring(9, 13) + str2.substring(14, 18);
        return str+temp;
    }
    @Test
    public void main() {
        String test = getUUID2();

        System.out.println(test);
    }
}

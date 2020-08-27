package com.example.demo.Util;
import org.junit.Test;

import java.util.UUID;
public class Getuuid {

    public static String geuuid(){
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        return uuid;
    }
    @Test
    public void main() {
        String test = geuuid();
        System.out.println(test);
    }
}

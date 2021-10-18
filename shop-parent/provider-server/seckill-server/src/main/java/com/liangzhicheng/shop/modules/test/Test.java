package com.liangzhicheng.shop.modules.test;

import java.util.StringTokenizer;

public class Test {

    public static void main(String[] args) {
        //字符串截取
        String value = "1,2,3";
        StringTokenizer stringTokenizer = new StringTokenizer(value, ",");
        while(stringTokenizer.hasMoreElements()){
            System.out.println(stringTokenizer.nextToken());
        }
    }

}

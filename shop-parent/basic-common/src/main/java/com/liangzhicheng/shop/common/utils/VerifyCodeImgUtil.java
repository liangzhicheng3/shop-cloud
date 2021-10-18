package com.liangzhicheng.shop.common.utils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class VerifyCodeImgUtil {

    private static char[] ops = new char[]{'+','-','*'};

    /**
     * @description 生成验证码
     * @return String
     */
    public static String generateVerifyCode() {
        Random rdm = generateRandom();
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        return "" + num1 + op1 + num2 + op2 + num3;
    }

    /**
     * @description 计算值
     * @param exp
     * @return Integer
     */
    public static Integer calc(String exp) {
        try{
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer) engine.eval(exp);
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * @description 创建验证码图片
     * @param verifyCode
     * @return BufferedImage
     */
    public static BufferedImage createVerifyCodeImg(String verifyCode) {
        int width = 80;
        int height = 32;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        Random rdm = generateRandom();
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        return image;
    }

    /**
     * @description 获取随机数
     * @return Random
     */
    private static Random generateRandom(){
        return new Random();
    }

//    public static void main(String[] args) {
//        String value = generateVerifyCode();
//        System.out.println("算术表达式：" + value + "=" + calc(value));
//    }

}


package com.liangzhicheng.shop.utils;

import com.liangzhicheng.shop.common.constant.Constants;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

public class MD5Util {

    /**
     * @description 明文加密为表单密码
     * @param password 明文密码
     * @return String
     */
    public static String encrypt(String password) {
        String salt = Constants.SALT;
        String str = "" + salt.charAt(0) + salt.charAt(2) + password + salt.charAt(4) + salt.charAt(6);
        return DigestUtils.md5Hex(str);
    }

    /**
     * @description 单次加密后的密文转换为数据库密文密码
     * @param password
     * @param salt
     * @return String
     */
    public static String encryptAgain(String password, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + password + salt.charAt(4) + salt.charAt(6);
        return DigestUtils.md5Hex(str);
    }

    public static void main(String[] args) {
        //111111 -> 1211111134 -> 8efd0d84097291edffe1e7774e4344de -> 128efd0d84097291edffe1e7774e4344de34 -> 027c528110bbd7cd4b93ebc661cb7dd7
        String input = "111111";
        String formPass = encrypt(input);
        System.out.println(formPass);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        System.out.println(uuid);
        String dbPass = encryptAgain(formPass, uuid);
        System.out.println(dbPass);
    }

}

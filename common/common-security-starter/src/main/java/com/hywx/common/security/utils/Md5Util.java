package com.hywx.common.security.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5盐值加解密 工具类
 * @author sun
 * @date 2018年5月22日 上午11:18:00
 */
public class Md5Util {

    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    //散列多次使用MD5
    public static String getMd5Pwd(String salt, String source, int hashIterations) {
        MessageDigest messageDigest = null;
        byte[] byteArray = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(salt.getBytes("UTF-8")); //先加盐
            byteArray = messageDigest.digest(source.getBytes("UTF-8")); //再放需要被加密的数据

            if(hashIterations < 1) hashIterations = 1;
            for(int i=0; i < hashIterations - 1; i++){
                messageDigest.reset();
                byteArray = messageDigest.digest(byteArray);
            }

        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }

    public static void main(String[] args) {
        String source = "12345678";
        String salt = "sbdp";
        //上边散列1次：f3694f162729b7d0254c6e40260bf15c
        //散列2次:36f2dfa24d0a9fa97276abbe13e596fc

        //散列两次实现原理
        System.out.println(getMd5Pwd(salt, source, 2));
    }
}
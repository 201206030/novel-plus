package com.java2nb.novel.core.utils;


import lombok.SneakyThrows;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

/**
 * @author 11797
 */
public class MD5Util {

    private static final String DEFAUL_CHARSET = "utf-8" ;

    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    @SneakyThrows
    public static String MD5Encode(String origin,String charsetname) {
        MessageDigest md = MessageDigest.getInstance("MD5");
        if (charsetname == null || "".equals(charsetname)) {
            return byteArrayToHexString(md.digest(origin
                    .getBytes()));
        } else {
            return byteArrayToHexString(md.digest(origin
                    .getBytes(charsetname)));
        }
    }

    @SneakyThrows
    public static String MD5New(String str) {
        //首先利用MD5算法将密码加密，变成等长字节
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b1 = md.digest(str.getBytes());
        //将等长字节利用Base64算法转换成字符串
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(b1);
    }

    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


}
package com.tmslibrary.utils;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class EncryptUtil {
    private static final String UTF8 = "utf-8";
    private static final String transform = "AES";

    private static final String algorithm = "AES";
    /**
     * 默认Java的AES最大支持128bit的密钥，
     * 如果使用256bit的密钥，会抛出一个异常：java.security.InvalidKeyException: Illegal key size 其实Java官网上提供了解决方案，
     * 需要下载“Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files for JDK/JRE 8”，替换JDK/JRE里的2个jar包。
     *
     * @param strKey
     * @return byte[]
     * @throws UnsupportedEncodingException
     */
    private static byte[] AESGetKey(String strKey) throws UnsupportedEncodingException {
        byte[] arrBTmp = strKey.getBytes(UTF8);
        byte[] arrB = new byte[32]; // 创建一个空的16/32位字节数组（默认值为0）
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        return arrB;
    }
    public static String AESEncrypt(String content, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance(transform);
        SecretKeySpec keySpec = new SecretKeySpec(AESGetKey(key), algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] stringByte=content.getBytes(UTF8);
        byte[] output = cipher.doFinal(stringByte, 0, stringByte.length);
        return new String(Base64.encode(output,Base64.DEFAULT));

    }
}

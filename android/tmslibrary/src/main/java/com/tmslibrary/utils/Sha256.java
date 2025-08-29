package com.tmslibrary.utils;

import android.text.TextUtils;


import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class Sha256 {

    public static String checkSum(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        Hasher hasher = Hashing.sha256().newHasher();
        if (hasher == null) {
            return null;
        }
        return hasher.putString(content, Charset.forName("UTF-8")).hash().toString();
    }

}

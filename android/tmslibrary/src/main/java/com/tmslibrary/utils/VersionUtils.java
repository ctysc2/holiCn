package com.tmslibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionUtils {
    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 比较版本号的大小
     * <p>
     * 1、前者大则返回一个正数
     * 2、后者大返回一个负数
     * 3、相等则返回0
     *
     * @param version1 版本号1
     * @param version2 版本号2
     * @return int
     */
    public static int compareVersion(String version1, String version2) {
        if (version1 == null || version2 == null) {
            return 0;
        }
        try {
            version1 = version1.replace("V","").replace("v","");
            version2 = version2.replace("V","").replace("v","");
            // 注意此处为正则匹配，不能用.
            String[] versionArray1 = version1.split("\\.");
            String[] versionArray2 = version2.split("\\.");
            int idx = 0;
            // 取数组最小长度值
            int minLength = Math.min(versionArray1.length, versionArray2.length);
            int diff = 0;
            // 先比较长度，如果长度相同，再比较大小
            // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大
        /*while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
                && (diff = Integer.parseInt(versionArray1[idx]) - Integer.parseInt(versionArray2[idx])) == 0) {
            ++idx;
        }*/
            while (idx < minLength
                    && (diff = Integer.parseInt(versionArray1[idx]) - Integer.parseInt(versionArray2[idx])) == 0) {
                ++idx;
            }
            diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
            return diff;
        }catch (Exception e){
            return 0;
        }
    }
}

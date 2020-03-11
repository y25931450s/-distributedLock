package com.xiaoying.base.lock.util;

import java.io.InputStream;


/**
 * 获取应用名字用于生成分布式锁的key
 */
public class AppUtils {

    private static volatile String appName = null;


    public static String loadAppName(String defaultDomain) {
        if (appName == null) {
            InputStream in = null;
            try {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream("/META-INF/app.properties");
                if (in == null) {
                    in = AppUtils.class.getResourceAsStream("/META-INF/app.properties");
                }
                if (in != null) {
                    java.util.Properties prop = new java.util.Properties();
                    prop.load(in);
                    appName = prop.getProperty("app.name");
                    if (appName != null) {
                        return appName;
                    }
                }
                if (appName == null) {
                    appName = defaultDomain;
                }
            } catch (Exception var15) {
                appName = defaultDomain;
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception var14) {
                        ;
                    }
                }

            }
        }
        return appName;
    }
}

package com.other;

import org.yifan.hao.DateUtil;

public class TestUtils {
    private static boolean flag1 = true;
    private static int mClickNum;

    /**
     * @return 首次返回 true
     */
    public static boolean getBoolean() {
        return flag1 = !flag1;
    }

    public static int setClickNum() {
        return ++mClickNum;
    }

    public static int reSetClickNum() {
        return mClickNum = 0;
    }

    public static String serviceSend() {
        return "服务端:" + DateUtil.formatCurrentDate(DateUtil.REGEX_DATE_TIME) + "\n你好客户端";
    }

    public static String clientSend() {
        return "客户端:" + DateUtil.formatCurrentDate(DateUtil.REGEX_DATE_TIME) + "\n你好服务端";
    }

    public static void threadSleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
        }
    }
}

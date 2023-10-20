package hao;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {

    /**
     * yyyy-MM-dd
     */
    public static final String REGEX_DATE = "yyyy-MM-dd";

    /**
     * kk:mm
     */
    public static final String REGEX_TIME = "kk:mm";

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String REGEX_DATE_TIME = "yyyy-MM-dd kk:mm:ss";

    /**
     * yyyy-MM-dd HH:mm:ss:SSS
     */
    public static final String REGEX_DATE_TIME_MILL = "yyyy-MM-dd kk:mm:ss:SSS";

    /**
     * yyyy年MM月dd日
     */
    public static final String REGEX_DATE_CHINESE = "yyyy年MM月dd日";

    /**
     * yyyy年MM月dd日 kk:mm
     */
    public static final String REGEX_DATE_TIME_CHINESE = "yyyy年MM月dd日 kk:mm";

    private static Map<String, SimpleDateFormat> formatterMap;

    /**
     * 获取当前日期或时间的字符串
     */
    public static String formatDate(String regex) {
        return getFormatter(regex).format(new Date());
    }

    /**
     * 获取指定日期或时间的字符串
     */
    public static String formatDate(String regex, Date date) {
        return getFormatter(regex).format(date);
    }

    /**
     * 获取指定日期或时间的字符串
     */
    public static String formatDate(String regex, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return getFormatter(regex).format(calendar.getTime());
    }

    /**
     * 获取指定日期或时间的字符串
     */
    public static String formatDate(String tagRegex, String srcRegex, String dateStr) {
        try {
            Date date = getFormatter(srcRegex).parse(dateStr);
            return getFormatter(tagRegex).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定日期或时间的Calendar对象
     */
    public static Calendar getCalendar(String regex, String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(getFormatter(regex).parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * @param seconds 时间字符串(例如:1600421227)
     * @param format  格式
     * @return 转换后的格式
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds + "000")));
    }

    public static String formatTime(long time) {
        time = time / 1000;

        //如果时间小于60秒，则返回秒数
        if (time / 60 == 0) {
            return time + "秒";
        }

        //如果时间小于1小时，则返回分钟数
        if (time / (60 * 60) == 0) {
            return (time / 60) + "分" + (time % 60) + "秒";
        }

        //如果时间小于1天，则返回小时数
        if (time / (60 * 60 * 24) == 0) {
            long hour = time / (60 * 60);
            time = time % (60 * 60);
            return hour + "小时" + (time / 60) + "分" + (time % 60) + "秒";
        }

        long day = time / (60 * 60 * 24);
        time = time % (60 * 60 * 24);

        long hour = time / (60 * 60);
        time = time % (60 * 60);

        return day + "天" + hour + "小时" + (time / 60) + "分";
    }

    public static String formatSendDate(String regex, String date) {
        //首先获取到传入的日期和当前日期
        Calendar calendar = getCalendar(regex, date);
        Calendar current = Calendar.getInstance();

        //首先判断是否为当天或者3天以内的邮件
        int currentDay = current.get(Calendar.DAY_OF_YEAR);
        int calendarDay = calendar.get(Calendar.DAY_OF_YEAR);
        //如果是当天的邮件，则返回15:30这样格式的日期
        if (currentDay == calendarDay) {
            return "今天 " + getFormatter(REGEX_TIME).format(calendar.getTime());
        }

        //如果是昨天的邮件，则返回昨天 15:30这样格式的日期
        if (currentDay - calendarDay == 1) {
            return "昨天 " + getFormatter(REGEX_TIME).format(calendar.getTime());
        }

        //如果是前天的邮件，则返回前天 15:30这样格式的日期
        if (currentDay - calendarDay == 2) {
            return "前天 " + getFormatter(REGEX_TIME).format(calendar.getTime());
        }

        //然后判断是否为同一年
        int currentYear = current.get(Calendar.YEAR);
        int calendarYear = calendar.get(Calendar.YEAR);
        //如果不是同一年，则返回年-月-日 时:分:秒这样格式的日期
        if (currentYear != calendarYear) {
            return getFormatter("yyyy-MM-dd kk:mm").format(calendar.getTime());
        }

        //否则返回1月1日 15:02这样格式的日期
        return getFormatter("MM月dd日 kk:mm").format(calendar.getTime());
    }

    private static SimpleDateFormat getFormatter(String regex) {
        if (formatterMap == null) {
            formatterMap = new HashMap<>();
        }

        SimpleDateFormat formatter = formatterMap.get(regex);
        if (formatter == null) {
            formatter = new SimpleDateFormat(regex, Locale.getDefault());
            formatterMap.put(regex, formatter);
        }

        return formatter;
    }

    public static String getHourStr(String timeStr) {
        String[] times = timeStr.split(":");

        int hours = Integer.parseInt(times[0]) * 3600;
        int minutes = Integer.parseInt(times[1]) * 60;
        int seconds = Integer.parseInt(times[2]);

        double time = (hours + minutes + seconds) / 3600.0;

        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(time);
    }

    public static String getWeekStr(int year, int month, int day) {
        String[] arr = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return arr[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

    public static String formatCurrentDate(String regex) {
        return getFormatter(regex).format(new Date(System.currentTimeMillis()));
    }

    /**
     * @return 时间戳
     */
    public static String getTimeStamp() {
        return getFormatter("yyyyMMddkkmmssSS").format(new Date(System.currentTimeMillis()));
    }
}
//G　　"公元"
//y　　四位数年份
//M　　月
//d　　日
//h　　时 在上午或下午 (1~12)
//H　　时 在一天中 (0~23)
//m　　分
//s　　秒
//S　　毫秒
//
//
//E　　一周中的周几
//D　　一年中的第几天
//w　　一年中第几个星期
//a　　上午 / 下午 标记符
//k 　　时(1~24)
//K 　   时 在上午或下午 (0~11)
//
//
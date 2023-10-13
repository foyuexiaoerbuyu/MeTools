package org.yifan.hao;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

/**
 * Created by ray on 2018/1/19.
 * String 字符串相关类方法
 */

public class StringUtil {

    /**
     * 作用:                  去除来都结尾的逗号
     *
     * @param stringBuilder 要操作的字符串
     * @return 截取开头结尾之后的字符串
     */
    public static StringBuilder subStrBuildComma(StringBuilder stringBuilder) {
        if (stringBuilder != null && !"".equals(stringBuilder.toString())) {
            StringBuilder stringBuilder1 = stringBuilder;
            if (!"".equals(stringBuilder.toString())) {
                if (stringBuilder.indexOf(",") == 0) {
                    String substring = stringBuilder.substring(1);
                    stringBuilder1 = new StringBuilder(substring);
                    return subStrBuildComma(stringBuilder1);
                } else if (stringBuilder.lastIndexOf(",") == stringBuilder.length() - 1) {
                    if (stringBuilder.length() >= 1) {
                        String substring = stringBuilder.substring(0, stringBuilder.length() - 1);
                        stringBuilder1 = new StringBuilder(substring);
                        return subStrBuildComma(stringBuilder1);
                    }
                }
            }
            return stringBuilder1;
        }
        return stringBuilder;
    }

    /**
     * 如果参数为null或者"null"字符串返回""空字符串
     * 注:org.apache.commons.lang.StringUtils#clean效果类似不同点在于"null"字符串不会返回""空字符串
     *
     * @param str 要判断的字符串
     * @return 字符串
     */
    public static String valueOf(Object str) {
        return isBlank(String.valueOf(str)) ? "" : String.valueOf(str);
    }

    /**
     * 如果参数为null或者"null"字符串返回""空字符串
     * 注:org.apache.commons.lang.StringUtils#clean效果类似不同点在于"null"字符串不会返回""空字符串
     *
     * @param str 要判断的字符串
     * @return 字符串
     */
    public static String valueOf(String str) {
        return isBlank(str) ? "" : str;
    }

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        return value == null || "".equalsIgnoreCase(value.trim());
    }

    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isBlank(String value) {
        return value == null || "".equalsIgnoreCase(value.trim()) || "null".equalsIgnoreCase(value.trim());
    }

    /**
     * 一次只能替换一个属性
     *
     * @param resStr      要操作的网页style
     * @param oldStylePrt 样式属性
     * @return 操作后的网页代码
     */
    public static String optionHtmlStyle(String resStr, String oldStylePrt, String newStylePrt) {
        if (resStr.contains(oldStylePrt)) {
            String temp = resStr.substring(resStr.indexOf(oldStylePrt));
            resStr = resStr.replace(temp.substring(0, temp.indexOf(";") + 1), newStylePrt);
        }
        return resStr;
    }

    /**
     * @param str    源字符串("","null","  ",以上三种都为"")
     * @param defVal 默认字符串
     * @return 字符串为空时的默认值
     */
    public static String defaultIfBlank(String str, String defVal) {
        return isBlank(str) ? defVal : str;
    }

    public static int parseInt(String str, int defVal) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return defVal;
        }
    }

    /**
     * @return 去除字符串所有中文
     */
    public static String replaceAllChinese(String str) {
        if (str == null || str.trim().length() == 0) {
            return "";
        }
        //// 中文正则
        return Pattern.compile("[\u4e00-\u9fa5]").matcher(str).replaceAll("");
    }

    /**
     * @return 统计出现次数
     */
    public static int countMatches(String str, String target) {
        int count = 0;
        int index = 0;
        while (index != -1) {
            index = str.indexOf(target, index);
            if (index != -1) {
                count++;
                index += target.length();
            }
        }
        return count;
    }

    /**
     * 统计出现次数
     */
    public static int countMatches(String str, char symbol) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == symbol) {
                count++;
            }
        }
        return count;
    }

    /**
     * 按行读取字符串
     */
    public static void readStrByLins(String str, IReadLin iReadLin) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8));
            String line;
            while ((line = br.readLine()) != null) {
                iReadLin.readLin(line.trim());
            }
        } catch (IOException e) {
            iReadLin.readLinEx(e);
        }
    }

    /**
     * 首字母转大写
     *
     * @param s
     * @return
     */
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

    /**
     * 作用:       去除开头结尾的逗号
     *
     * @param str 要操作的字符串
     * @return 截取后的字符串
     */
    private String subStrComma(String str) {
        if (!"".equals(str) && str != null) {
            if (str.indexOf(",") == 0) {
                String substring = str.substring(1);
                return subStrComma(substring);
            } else if (str.lastIndexOf(",") == str.length() - 1) {
                String substring = str.substring(0, str.length() - 1);
                return subStrComma(substring);
            }
        }
        return str;
    }

    /**
     * 作用:       移除开头结尾,指定的字符串
     *
     * @param str    要操作的字符串
     * @param symbol 要去除的字符
     * @return 截取后的字符串
     */
    private String clearSymbol(String str, String symbol) {
        if (str != null && !"".equals(str)) {
            if (str.indexOf(symbol) == 0) {
                String substring = str.substring(1);
                return clearSymbol(substring, symbol);
            } else if (str.lastIndexOf(symbol) == str.length() - 1) {
                String substring = str.substring(0, str.length() - 1);
                return clearSymbol(substring, symbol);
            }
        }
        return str;
    }

    public interface IReadLin {
        void readLin(String lin);

        default void readLinEx(IOException exception) {

        }
    }


}

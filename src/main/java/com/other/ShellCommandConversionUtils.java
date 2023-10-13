package com.other;

/**
 * https://blog.csdn.net/zzyawei/article/details/80816717
 * adb shell 复制/移动文件时的转义字符
 */
public class ShellCommandConversionUtils {

    private static final char[] SPECIAL_CHARS = new char[]{'*', '?', '[', ']', '{', '}', ' ', 0x0D, '=',  '>', '<', '|', '&', '(', ')', ';', '!', '\\', '\'', '"'};

    public static String convert(String line) {
        char[] chars = line.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            for (int j = 0; j < SPECIAL_CHARS.length; j++) {
                if (c == SPECIAL_CHARS[j]) {
                    chars = insert(chars, i);
                    i++;
                }
            }
        }
        return new String(chars);
    }

    private static char[] insert(char[] chars, int index) {
        char[] nChars = new char[chars.length + 1];
        System.arraycopy(chars, 0, nChars, 0, index + 1);
        nChars[index] = '\\';
        System.arraycopy(chars, index, nChars, index + 1, chars.length - index);
        return nChars;
    }
}

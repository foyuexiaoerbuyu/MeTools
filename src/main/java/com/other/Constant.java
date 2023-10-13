package com.other;


import org.yifan.hao.FileUtils;

import java.io.File;

public class Constant {

    /**
     * 配置文件路径 C:\Users\HP\MyZhiHuConfig
     */
    public static final String CONFIG_PATH = FileUtils.getSysPath("user.home") + "MyZhiHuConfig" + File.separator;
    /**
     * 缓存目录 C:\Users\Mi\MyZhiHuConfig\cache
     */
    public static final String PATH_CACHE_DIR = CONFIG_PATH + "cache" + File.separator;
    public static final String PATH_FILE_DIR = CONFIG_PATH + "file" + File.separator;

    public static final String CUT_TMP_PATH = PATH_CACHE_DIR + "剪切板临时文件.txt";
    /**
     * 配置文件路径 C:\Users\HP\MyZhiHuConfig
     */
    public static final String CONFIG_FILE_PATH = CONFIG_PATH + "config.json";

    public static final String CONFIG_KEY_SAVE_PATH = "defSavePath";
    public static final String CONFIG_KEY_CATEGORY = "saveCategory";
    public static final String CONFIG_KEY_ADBS = "CONFIG_KEY_ADBS";
    public static final String CONFIG_VALUE_CATEGORY = "文档;社会;历史人物传记;笑一笑;赚钱";

    /**
     * 默认保存路径
     */
    public static final String DEF_SAVE_PATH = "F:/知乎/";

    /**
     * 暂时禁用收藏链接
     */
    public static final String ENABLE_TXT_TFCOLLECTIONLINK = "暂时禁用收藏链接";


    public static boolean TAG_ADD_CATEGORY = false;
    public static String path_tmp_clipboard_1 = CONFIG_PATH + "clipboard_1.txt";
    public static String path_tmp_clipboard_2 = CONFIG_PATH + "clipboard_2.txt";
    public static String path_tmp_clipboard_3 = CONFIG_PATH + "clipboard_3.txt";

    public static String str_pull_file = "拉取文件/文件夹";

//    /**
//     * 获取adb命令合集
//     *
//     * @return
//     */
//    public static LinkedHashMap<Integer, String> getAdbCommands() {
//        //key:adb命令 val:名称
//        LinkedHashMap<Integer, String> hashMap = new LinkedHashMap<>();
//        hashMap.put(0x001, "点击卸载当前app");
//        return hashMap;
//    }


}

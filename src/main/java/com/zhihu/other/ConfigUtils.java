package com.zhihu.other;

import com.other.Constant;
import com.other.GsonUtils;
import org.yifan.hao.FileUtils;


public class ConfigUtils {
    //{"saveCategory":"文档;社会;历史人物传记;笑一笑;赚钱;实用;素材"}
    public static ConfigBen getConfig() {
        String s = FileUtils.readFile(Constant.CONFIG_FILE_PATH);
        return GsonUtils.fromJson(s, ConfigBen.class);
    }

    public static void saveConfig(ConfigBen configBen) {
        FileUtils.writeFile(Constant.CONFIG_FILE_PATH, GsonUtils.toJson(configBen));
    }
}

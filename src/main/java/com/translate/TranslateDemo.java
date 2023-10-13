package com.translate;


import com.other.GsonUtils;
import com.translate.utils.AuthV3Util;
import com.translate.utils.HttpUtil;
import com.translate.utils.TranslateBean;
import org.yifan.hao.WinUtils;


import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 网易有道智云翻译服务api调用demo
 * api接口: https://openapi.youdao.com/api
 */
public class TranslateDemo {

    private static final String APP_KEY = "10e7171055b0ff2c";     // 您的应用ID
    private static final String APP_SECRET = "pcYIsIwq7cIttE0XAsO69qz9p5kfqxdL";  // 您的应用密钥

    public static void startTranslators(String[] args) throws NoSuchAlgorithmException {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams("警告");
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api", null, params, "application/json");
        // 打印返回结果
        if (result != null) {
            System.out.println(new String(result, StandardCharsets.UTF_8));
        }
//        main("警告");
        System.exit(1);
    }

    public static String startTranslators(String args) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        // 添加请求参数
        Map<String, String[]> params = createRequestParams(args);
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api", null, params, "application/json");
        // 打印返回结果
        if (result != null) {
            String str = new String(result, StandardCharsets.UTF_8);
            TranslateBean translateBean = GsonUtils.fromJson(str, TranslateBean.class);
            List<String> translations = translateBean.getTranslation();
            List<TranslateBean.TranslateWeb> webs = translateBean.getWeb();
            for (TranslateBean.TranslateWeb web : webs) {
                sb.append(web.getKey()).append("\n");
                List<String> value = web.getValue();
                for (String s : value) {
                    sb.append(s).append("\n");
                }
            }
            System.out.println(str);
            System.out.println(sb);
            WinUtils.setSysClipboardText(sb.toString());
            return sb.toString();
        }

        return null;
    }

    private static Map<String, String[]> createRequestParams(String queryStr) {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html
         */
        String q = queryStr;
        String from = "auto";
        String to = "general";
//        String vocabId = "您的用户词表ID";

        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("from", new String[]{from});
            put("to", new String[]{to});
//            put("vocabId", new String[]{vocabId});
        }};
    }
}

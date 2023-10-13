package com.zhihu.other;

import com.alibaba.fastjson.JSONObject;
import com.other.Constant;



import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.yifan.hao.FileUtils;
import org.yifan.hao.StringUtil;
import org.yifan.hao.XLog;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommUtils {

    /**
     * @param html html
     * @return 格式化后的html
     */
    public static String formatHtml(String html) {
        if (StringUtils.isNotBlank(html)) {
            try {
                Document doc = Jsoup.parseBodyFragment(html);
                html = doc.body().html();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //居中显示
        return html.replace("<div class=\"Question-main\">", "<div class=\"Question-main\" style=\"justify-content: center\"> ");
    }

    /**
     * 替换html里的图片链接
     *
     * @param html html
     * @return 格式化后的html
     */
    public static String replaceHtmlPicSrc(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("img[src]");//获取到的值为所有的<embed src="...">
        for (Element element : elements) {
            String picUrl = element.attr("data-original");
            if (StringUtils.isBlank(picUrl)) {
                picUrl = element.attr("data-actualsrc");
            }
            element.attr("src", picUrl);
        }
        return doc.html();
    }

    /**
     * @param html html
     * @return html标题
     */
    public static String getHtmlTitle(String html) {
        return Jsoup.parse(html).title();
    }

    /**
     * 去除知乎html循环请求
     *
     * @param html html
     * @return 格式化后的html
     */
    public static String removeLoopRequest(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select("script[src]");//获取到的值为所有的<embed src="...">
        for (Element element : elements) {
            element.attr("src", "");
        }
        return doc.html();
    }


    /**
     * 字符串是否包含uri连接
     *
     * @param urlData 包含网址url的文本
     * @return 正则提取到的url
     */
    public static boolean isContainUrl(String urlData) {
        return StringUtil.isEmpty(getUrl(urlData));
    }

    /**
     * @param urlData 包含网址url的文本
     * @return 正则提取到的url
     */
    public static String getUrl(String urlData) {
        return filterSpecialStr(
                "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]", urlData);
    }

    /**
     * @param html html文本
     * @return 调整后的html
     */
    public static String removeOther(String html) {
        Document doc = Jsoup.parse(html);
        //1.增加标题
        String title = doc.selectFirst("h1").text();
        if (StringUtils.isBlank(title)) {
            title = doc.selectFirst("H1").text();
        }
        System.out.println("获取到的数据title:" + title);
        doc.title(title);
        doc.select("div[class=Question-mainColumn]").append(title);
        //2.去除标题空格
        doc.select("div[class=QuestionHeader]").remove();
        //3.
        return doc.toString();
    }

    /**
     * 参数1 regex:我们的正则字符串 参数2 就是一大段文本，这里用data表示
     */
    private static String filterSpecialStr(String regex, String data) {
        //sb存放正则匹配的结果
        StringBuilder sb = new StringBuilder();
        //编译正则字符串
        Pattern p = Pattern.compile(regex);
        //利用正则去匹配
        Matcher matcher = p.matcher(data);
        //如果找到了我们正则里要的东西
        while (matcher.find()) {
            //保存到sb中，"\r\n"表示找到一个放一行，就是换行
            sb.append(matcher.group()).append("\r\n");
        }
        return sb.toString();
    }


    /**
     * 保存私有信息
     */
    public static String getPrivateInfo(String key, String defVal) {
        if (!FileUtils.isFileExist(Constant.CONFIG_FILE_PATH)) {
            FileUtils.writeFile(Constant.CONFIG_FILE_PATH, "{}");
            return defVal;
        }
        String text = FileUtils.readFile(Constant.CONFIG_FILE_PATH, "utf-8");
        if (StringUtil.isBlank(text)) {
            return defVal;
        }
        JSONObject jsonObject = JSONObject.parseObject(text);
        String val = jsonObject.getString(key);
        if (StringUtil.isBlank(val)) {
            return defVal;
        }
        return val;
    }

    /**
     * 获取私有信息
     */
    public static void setPrivateInfo(String key, String value) {
        try {
            JSONObject privateInfo;
            if (FileUtils.isFileExist(Constant.CONFIG_FILE_PATH)) {
                privateInfo = JSONObject
                        .parseObject(FileUtils.readFile(Constant.CONFIG_FILE_PATH, "utf-8"));
            } else {
                FileUtils.writeFile(Constant.CONFIG_FILE_PATH, "{}");
                privateInfo = new JSONObject();
            }
            privateInfo.put(key, value);
            FileUtils.writeFile(Constant.CONFIG_FILE_PATH, privateInfo.toJSONString());
        } catch (Exception e) {
            XLog.printExceptionInfo(e);
            FileUtils.deleteFile(Constant.CONFIG_FILE_PATH);
        }
    }


    public static String convert(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i != 0) {
                    output.append('_');
                }
                output.append(Character.toLowerCase(c));
            } else if (c == ' ') {
                output.append('_');
            } else {
                output.append(c);
            }
        }
        return output.toString();
    }

    public static String getImgFile(String fileName) {
        return "G:\\MWeb\\webUtils\\Mytools\\src\\main\\resources\\" + fileName;
    }


    /**
     * @param filePath java读取文件前五行并保存
     */
    public static void readFileTop5LineAndSave(String filePath, int readLineNum) {
        try {
            // 读取文件内容
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
            StringBuilder fileContent = new StringBuilder();
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append(System.lineSeparator());
                lineCount++;
                if (lineCount == readLineNum) {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(fileContent.toString().trim()), null);// 复制前五行到剪贴板
                    fileContent.setLength(0); // 清空内容
                }
            }
            reader.close();
            // 如果剩余行数小于5，则不保存到源文件
            if (lineCount < readLineNum) {
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(fileContent.toString().trim()), null);// 复制前五行到剪贴板
                fileContent.setLength(0); // 清空内容
            }
            // 剩余内容保存到源文件
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8));
            writer.write(fileContent.toString().trim());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
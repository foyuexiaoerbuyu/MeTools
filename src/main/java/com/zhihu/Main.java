package com.zhihu;

import cn.edu.hfut.dmic.contentextractor.ContentExtractor;
import cn.edu.hfut.dmic.contentextractor.News;
import com.overzealous.remark.Remark;
import org.yifan.hao.FileUtils;


public class Main {

    static long maxSize;
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
//        zhihu("社会");
//        m1();

    }

    private static void m1() throws Exception {
        //QuestionAnswer-content
//        String filename = "D:/tmp/学习spring框架，是先学ssm还是直接学springboot？ - 知乎2023041716001114.html";
//        String s = FileUtils.readFile(filename, "UTF-8");
//        System.out.println("s = " + s);
//        String url = "https://zhuanlan.zhihu.com/p/476994749";
        String url = "https://www.zhihu.com/question/509707651/answer/2527582446";
//        Document doc = new Document(url);
//        JsoupUtils.makeAbs(doc, url);
        News news = ContentExtractor.getNewsByUrl(url);
        Remark remark = new Remark();
        String content = remark.convert(news.getContentElement().html());
        FileUtils.writeFile("D:/tmp/md " + System.currentTimeMillis() + ".md", content);
//        FileUtils.writeFile("D:/tmp/txt " + System.currentTimeMillis() + ".txt", news.getContent());
        FileUtils.writeFile("D:/tmp/html" + System.currentTimeMillis() + ".html", news.getContentElement().html());

    }

    private static void zhihu(String postion) {
//        String articleUrl = WinUtils.getSysClipboardText();
//        String filePath = Constant.DEF_SAVE_PATH + postion;
        String filePath = "D:/tmp";
        String articleUrl = "https://zhuanlan.zhihu.com/p/476994749";
        ZhihuUtils.saveArticle(filePath, articleUrl,
                (saveName, saveSuccess) -> {
//                WindowAlert.display("提示", saveName);
                });
//        ZhihuDownload.saveCollect("D:\\2020-10-21\\测试", "https://www.zhihu.com/question/20784865/answer/1544077523");
    }

}
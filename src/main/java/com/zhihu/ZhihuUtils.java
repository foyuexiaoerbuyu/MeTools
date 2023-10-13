package com.zhihu;

import com.google.gson.Gson;
import com.other.Constant;




import com.zhihu.other.BaseUrl;
import com.zhihu.other.CollectionEnt;
import com.zhihu.other.CollectionEnt.DataBean;
import com.zhihu.other.CommUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import okhttp3.Call;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.yifan.hao.DateUtil;
import org.yifan.hao.FileUtils;
import org.yifan.hao.StringUtil;
import org.yifan.hao.XLog;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ZhihuUtils {

    private static String okhttpTag = "zhihuTag";

    /**
     * 保存知乎文章到本地
     *
     * @param filePath   保存路径
     * @param articleUrl 文章url
     */
    public static void saveArticle(String filePath, String articleUrl, SaveCallback saveCallback) {
        if (StringUtil.isBlank(articleUrl) || !StringUtils.contains(articleUrl, "zhihu")) {
            saveCallback.callback("链接不能为空:\n" + articleUrl, false);
            return;
        }
        if (!articleUrl.startsWith("http") || !articleUrl.startsWith("wwww.") || !articleUrl
                .startsWith("https.")) {
            articleUrl = CommUtils.getUrl(articleUrl);
            if (!articleUrl.startsWith("http") && !articleUrl.startsWith("https") && !articleUrl
                    .startsWith("wwww.")) {
                saveCallback.callback("文章链接错误,请检查链接是否正确: " + articleUrl, false);
                return;
            }
        }
        FileUtils.makeDirs(filePath);
        boolean saveSuccess = false;
        String htmlTitle = "";
        try {
            String response = OkHttpUtils.get().url(articleUrl).tag(okhttpTag)
                    .build().execute().body().string();
            //1.获取标题
            htmlTitle = FileUtils.replaceIllegalStr(CommUtils.getHtmlTitle(response))
                    + DateUtil.getTimeStamp();
            //2.去除循环请求
            String html = CommUtils.removeLoopRequest(response);
            html = CommUtils.removeOther(html);
            //3.解决图片不显示问题
            html = CommUtils.replaceHtmlPicSrc(html);
            //4.格式化html输出
            html = CommUtils.formatHtml(html);
            saveSuccess = FileUtils.writeFile(
                    filePath + File.separator + htmlTitle + ".html", html);
            if (saveSuccess) {
                System.out.println("保存成功");
            }

        } catch (IOException e) {
            XLog.printExceptionInfo(e);
            saveCallback.callback("保存失败:" + e.getMessage(), false);
            return;
        }
        saveCallback.callback("保存" + (saveSuccess ? "成功" : "失败"), saveSuccess);
    }

    /**
     * 保存知乎收藏到本地
     * <p>
     * 使用: saveCollect("D:\\2020-10-21\\文档", BaseUrl.getCollectionUrl("162366996"));
     *
     * @param filePath      保存路径
     * @param collectionUrl 收藏url   "https://www.zhihu.com/question/20784865/answer/1544077523"
     */
    public static void saveCollect(String filePath, String collectionUrl,
                                   SaveCallback saveCallback) {
        if (!collectionUrl.startsWith("https://www.") || !collectionUrl.startsWith("www.")) {
            collectionUrl = BaseUrl.getCollectionUrl(collectionUrl);
        }
        FileUtils.makeDirs(filePath);
        OkHttpUtils.get().url(collectionUrl).tag(okhttpTag)
                .build().execute(new Callback<CollectionEnt>() {
            @Override
            public CollectionEnt parseNetworkResponse(Response response, int i) throws Exception {
                //收藏
                if (response.body() != null) {
                    String string = response.body().string();
                    return new Gson().fromJson(string, CollectionEnt.class);
                }
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int i) {
                XLog.printExceptionInfo(e);
                call.cancel();
                saveCallback.callback(e.getMessage() + "\n具体异常信息请查看桌面异常日志", false);
                XLog.file("异常了" + e.getMessage());
            }

            @Override
            public void onResponse(CollectionEnt collectionEnt, int i) {
                //如果不是结尾页面就进行循环请求
                List<DataBean> data = collectionEnt.getData();
                for (DataBean dataBean : data) {
                    String ttitle;
                    if (StringUtils.isBlank((ttitle = dataBean.getContent().gettitle()))) {
                        ttitle = dataBean.getContent().getQuestion().getTitle();
                        if (StringUtils.isBlank(ttitle)) {
                            saveCallback
                                    .callback("标题为空:  " + dataBean.getContent().toString(), false);
                            return;
                        }
                    }
                    ttitle = FileUtils.replaceIllegalStr(ttitle) + DateUtil.getTimeStamp();
                    String htmlContent = CommUtils.formatHtml(dataBean.getContent().getContent());
                    boolean saveSuccess = FileUtils
                            .writeFile(filePath + File.separator + ttitle + ".html",
                                    ttitle + "\n\n\n" + htmlContent);
                    if (saveSuccess) {
                        System.out.println("保存成功:" + ttitle);
                    }
                }

                if (collectionEnt.getPaging().isIs_end()) {
                    OkHttpUtils.getInstance().cancelTag(okhttpTag);
                    saveCallback.callback("保存收藏完毕", true);
                    return;
                }
                //循环请求下一页
                saveCollect(filePath, collectionEnt.getPaging().getNext(), saveCallback);
            }
        });
    }

    /**
     * @return 获取分类数组
     */
    public static String[] getCategory() {
        return CommUtils
                .getPrivateInfo(Constant.CONFIG_KEY_CATEGORY, Constant.CONFIG_VALUE_CATEGORY)
                .split(";");
    }

    /**
     * 设置分类数组
     */
    public static void setCategory(String inputContent) {
        if (!StringUtil.isBlank(inputContent)) {
            CommUtils.setPrivateInfo(Constant.CONFIG_KEY_CATEGORY, inputContent);
        }
    }

    public static void setAdbs(String adb) {
        if (!StringUtil.isBlank(adb)) {
            CommUtils.setPrivateInfo(Constant.CONFIG_KEY_ADBS, adb);
        }
    }

    public static String getAdbsStr() {
        String privateInfo = CommUtils.getPrivateInfo(Constant.CONFIG_KEY_CATEGORY, null);
        if (privateInfo.length() == 0) {
            return privateInfo;
        }
        return privateInfo;
    }

    public static String[] getAdbs() {
        String privateInfo = CommUtils.getPrivateInfo(Constant.CONFIG_KEY_CATEGORY, null);
        if (privateInfo.length() == 0) {
            return null;
        }
        return privateInfo
                .split(";");
    }

    /**
     * @return 获取分类数组字符串
     */
    public static String getCategoryStr() {
        return CommUtils
                .getPrivateInfo(Constant.CONFIG_KEY_CATEGORY, Constant.CONFIG_VALUE_CATEGORY);
    }

    public interface SaveCallback {

        void callback(String saveMsg, boolean saveSuccess);
    }

}
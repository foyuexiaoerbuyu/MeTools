package com.zhihu.other;

public class BaseUrl {

    /**
     * @param collectioId 收藏夹id
     * @param offset 索引条数(从多少条开始)
     * @param limit 取条数(取多少条)
     * @return 收藏url
     */
    public static String getCollectionUrl(String collectioId) {
        return "https://www.zhihu.com/api/v4/collections/" + collectioId
            + "/items?offset=0&limit=20";
    }

    /**
     * @return 热搜url
     */
    public static String getHotSearchUrl(String arg) {
        return "https://www.zhihu.com/api/v4/search/preset_words?w=";
    }

    /**
     * @return 一篇文章
     */
    public static String getOneArticleUrl(String arg) {
        return "https://zhuanlan.zhihu.com/p/269558041";
    }

    /**
     * 专栏列表 https://www.zhihu.com/api/v4/columns/c_119120829/items 专栏文章
     * https://zhuanlan.zhihu.com/p/336589475
     *
     * @return 专栏列表
     */
    public static String getZhuanLanUrl(String column) {
        return "https://www.zhihu.com/api/v4/columns/" + column + "/items";
    }
}
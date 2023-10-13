package com.translate.utils;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TranslateBean {


    @SerializedName("returnPhrase")
    private List<String> returnPhrase;
    @SerializedName("query")
    private String query;
    @SerializedName("web")
    private List<TranslateWeb> web;
    @SerializedName("translation")
    private List<String> translation;
    @SerializedName("basic")
    private Basic basic;

    public List<String> getReturnPhrase() {
        return returnPhrase;
    }

    public void setReturnPhrase(List<String> returnPhrase) {
        this.returnPhrase = returnPhrase;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<TranslateWeb> getWeb() {
        return web;
    }

    public void setWeb(List<TranslateWeb> web) {
        this.web = web;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public static class Basic {
        @SerializedName("phonetic")
        private String phonetic;
        @SerializedName("explains")
        private List<String> explains;

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }
    }

    public static class TranslateWeb {
        @SerializedName("value")
        private List<String> value;
        @SerializedName("key")
        private String key;

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
package com.zhihu.other;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigBen {
    //{"saveCategory":"文档;社会;历史人物传记;笑一笑;赚钱;实用;素材"}
    @SerializedName("saveCategory")
    private String saveCategory;
    @SerializedName("cusBtns")
    private List<CusBtnsDTO> cusBtns;

    public String getSaveCategory() {
        return saveCategory;
    }

    public void setSaveCategory(String saveCategory) {
        this.saveCategory = saveCategory;
    }

    public List<CusBtnsDTO> getCusBtns() {
        return cusBtns;
    }

    public void setCusBtns(List<CusBtnsDTO> cusBtns) {
        this.cusBtns = cusBtns;
    }

    public static class CusBtnsDTO {
        @SerializedName("name")
        private String name;
        @SerializedName("id")
        private Long id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}

package com.netease.study.pushserver.Dto;

import java.util.List;
import java.util.Map;

/**
 * Created by hanpfei0306 on 18-7-19.
 */
public class ExtDto {
    private String biTag;
    private List<Map> customize;

    public ExtDto() {
    }

    public void setBiTag(String biTag) {
        this.biTag = biTag;
    }

    public String getBiTag() {
        return biTag;
    }

    public void setCustomize(List<Map> customize) {
        this.customize = customize;
    }

    public List<Map> getCustomize() {
        return customize;
    }
}

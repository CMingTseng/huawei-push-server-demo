package com.netease.study.pushserver.Dto;

public class NotifyMessageBodyDto {
    private String title;
    private String content;

    public NotifyMessageBodyDto() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

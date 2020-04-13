package com.lzm.voiceassistant.bean;

public class WebPageBean {
    private String header;
    private String url;

    public WebPageBean(String header, String url) {
        this.header = header;
        this.url = url;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

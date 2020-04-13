package com.lzm.voiceassistant.bean;

import java.util.List;

public class AssistantResponse {


    private String text;

    private Service service;

    private AnswerBean answer;

    private List<MoreResultsBean> moreResults;

    private WebPageBean webPage;

    private String dataJson;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public List<MoreResultsBean> getMoreResults() {
        return moreResults;
    }

    public void setMoreResults(List<MoreResultsBean> moreResults) {
        this.moreResults = moreResults;
    }

    public WebPageBean getWebPage() {
        return webPage;
    }

    public void setWebPage(WebPageBean webPage) {
        this.webPage = webPage;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }
}

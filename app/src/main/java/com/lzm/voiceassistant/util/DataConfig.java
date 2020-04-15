package com.lzm.voiceassistant.util;

import com.lzm.voiceassistant.bean.AnswerBean;
import com.lzm.voiceassistant.bean.AssistantResponse;
import com.lzm.voiceassistant.bean.Name;
import com.lzm.voiceassistant.bean.Service;
import com.lzm.voiceassistant.bean.WebPageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Morris
 * @email moon.liuzhimi@gmail.com
 * @date 2020-04-12 20:31
 */
public class DataConfig {

    public static List<Name> getOrders() {
        List<Name> orders = new ArrayList<>();
        orders.add(new Name("Apple"));
        orders.add(new Name("Banana"));
        orders.add(new Name("Strawberry"));
        return orders;
    }

    public static AssistantResponse getAssistantResponse() {
        AssistantResponse assistantResponse = new AssistantResponse();
        assistantResponse.setAnswer(new AnswerBean("", "TT"));
        assistantResponse.setDataJson("{\"name\": \"Lemon\"}");
        assistantResponse.setMoreResults(null);
        assistantResponse.setService(Service.DISPLAY_XXX);
        assistantResponse.setText("打开百度");
        assistantResponse.setWebPage(new WebPageBean(null, "https://www.baidu.com"));
        return assistantResponse;
    }
}

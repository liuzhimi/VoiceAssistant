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

    public static AssistantResponse getAssistantResponse(String s) {
        AssistantResponse assistantResponse = new AssistantResponse();
        if (s.contains("查看数据")) {
            assistantResponse.setAnswer(new AnswerBean("以下是为您查询到的数据", "TT"));
            assistantResponse.setService(Service.DISPLAY_XXX);
        } else if (s.contains("你在干什么")) {
            assistantResponse.setAnswer(new AnswerBean("你猜我在干什么", "TT"));
            assistantResponse.setService(Service.COMMUNICATION);
        } else if (s.contains("查看百度页面")) {
            assistantResponse.setAnswer(new AnswerBean("这是您想要查看的网页", "TT"));
            assistantResponse.setService(Service.OPEN_XXX);
            assistantResponse.setWebPage(new WebPageBean(null, "https://www.baidu.com"));
        } else {
            assistantResponse.setAnswer(new AnswerBean("好的，正在退出", "TT"));
            assistantResponse.setService(Service.CLOSE_XXX);
        }

        assistantResponse.setDataJson("{\"name\": \"Lemon\"}");
        assistantResponse.setMoreResults(null);
        assistantResponse.setText(s);

        return assistantResponse;
    }
}

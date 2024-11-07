package com.demo.constrants;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    String event;
    String content;
    String username;
    String error = "";
    int status = STATUS_SUCCESS_CODE;

    public static final int STATUS_SUCCESS_CODE = 0;
    public static final int STATUS_REJECT_CODE = 1;

    public Message(String event, String content, String username) {
        this.event = event;
        this.content = content;
        this.username = username;
    }

    public Message(String content) {
        this.event = MessageEvent.USER_MESSAGE;
        this.content = content;
        this.status = STATUS_SUCCESS_CODE;
    }

    public void sendUserMessage(String content) {
        this.event = MessageEvent.USER_MESSAGE;
        this.content = content;
        this.status = STATUS_SUCCESS_CODE;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}

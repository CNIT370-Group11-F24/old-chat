package com.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.demo.constrants.LoginForm;
import com.demo.constrants.Message;
import com.demo.constrants.MessageEvent;

import java.io.PrintWriter;

public class MessageSendUtil {
    private static final String EVENT = "event";
    public static void sendMessage(PrintWriter out, String message) {
        sendMessage(out, message, MessageEvent.SYS_MESSAGE, 0);
    }


    public static void sendMessage(PrintWriter out, String message, String username) {

        sendMessage(out, message, MessageEvent.USER_MESSAGE,username, 0);
    }

    public static void sendMessage(PrintWriter out, String message, String event, int status) {
        sendMessage(out, message, event, null, 0);
    }

    public static void sendMessage(PrintWriter out, String message, String event,String username, int status) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message);
        jsonObject.put("status", status);
        jsonObject.put("username", username);
        jsonObject.put(EVENT, event);
        out.println(jsonObject.toJSONString());
    }

    public static Message readMessage(String message) {
        JSONObject jsonObject = JSONObject.parseObject(message);
        String event = jsonObject.getString(EVENT);
        String content = jsonObject.getString("content");
        String error = jsonObject.getString("error");
        String username = jsonObject.getString("username");
        int status = jsonObject.getInteger("status");
        return new Message(event, content,username,error, status  );
    }

    public static LoginForm readLoginMessage(String content) {
        LoginForm loginForm = JSONObject.parseObject(content, new TypeReference<LoginForm>(){});
        return loginForm;
    }
}

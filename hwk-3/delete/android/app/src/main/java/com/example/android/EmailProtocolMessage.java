package com.example.android;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EmailProtocolMessage {

    public static final String TYPE_KEY = "type";
    public static final String USERNAME_KEY = "username";
    public static final String EMAIL_KEY = "email";
    public static final String EMAILS_KEY = "emails";
    public static final String STATUS_KEY = "status";
    public static final String LOGIN_KEY = "log_in";
    public static final String PASSWORD_KEY = "password";
    public static final String TOKEN_KEY = "token";

    public static final String LOGIN_COMMAND_OLD = "log_in";
    public static final String LOGIN_COMMAND = "log_in";
    public static final String SENDEMAIL_COMMAND = "send_email";
    public static final String RETRIVE_EMAIL_COMMAND = "retrieve_emails";
    public static final String LOGOUT_COMMAND = "log_out";
    public static final String LOGIN_ACK_OLD = "log_in_ack";
    public static final String LOGIN_ACK = "login_ack";
    public static final String LOGOUT_ACK = "log_out_ack";
    public static final String SENDEMAIL_ACK = "send_email_ack";
    public static final String OK_STATUS = "ok";
    public static final String FAILED_STATUS = "failed";
    public static final String INVALID_TOKEN_STATUS = "invalid token";
    private final Hashtable<String, String> dict = new Hashtable<String, String>();

    // TODO: Test this function
    public String toString() {
        List<String> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : dict.entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();
            String seg = key + "=" + val;
            pairs.add(seg);
        }
        return String.join("&", pairs);
    }

    public String getParam(String key) {
        // TODO: add error checking ?
        return dict.get(key);
    }

    public void putParam(String key, String command) {
        dict.put(key, command);
    }

    public EmailProtocolMessage() {
    }

    public EmailProtocolMessage(String message) {
        String[] companies = message.split("&");
        for (String pair : companies) {
            String[] keyValue = pair.split("=");
            if (keyValue.length != 2) {
                putParam(TYPE_KEY, "ERROR");
                return;
            }
            String key = keyValue[0];
            String value = keyValue[1];
            putParam(key, value);
        }
    }
}

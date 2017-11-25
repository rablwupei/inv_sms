package org.j2megame.invsms;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by wupei on 2017/11/25.
 */

public class Weixin {

    public static class Message {
        public String touser = "wupei";
        public String message;

        public Message(String message) {
            this.message = message;
        }

        public Message touser(String touser) {
            this.touser = touser;
            return this;
        }
    }

    static String corpID = "wx49ef3962c2f3a2c0";
    static String secret = "l4mKdYE-C1VCROkcjmYoMv8dOQ3w1mF35L0dePiRXbL_SW5MMG7AN5tnURhAGvmb";

    static String access_token = "";

    public static void send(final Message msg) {
        if (access_token.length() == 0) {
            _initAccessToken(new Callback() {
                @Override
                public void run() {
                    _doSend(msg);
                }
            });
        } else {
            _doSend(msg);
        }
    }

    static void _doSend(final Message msg) {
        JSONObject json = new JSONObject();
        try {
            JSONObject jsonContent = new JSONObject();
            jsonContent.put("content", msg.message);
            json.put("text", jsonContent);
            json.put("touser", msg.touser);
            json.put("toparty", "");
            json.put("totag", "");
            json.put("msgtype", "text");
            json.put("agentid", 0);
            json.put("safe", 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        post(String.format("https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s", access_token), json, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //System.out.println("weixin send: " + msg.message);
                int errcode = response.optInt("errcode");
                if (errcode == 40014) {
                    _initAccessToken(new Callback() {
                        @Override
                        public void run() {
                            _doSend(msg);
                        }
                    });
                } else if (errcode != 0) {
                    System.out.println("weixin send error. " + response.toString());
                }
            }
        });
    }

    static void _initAccessToken(final Callback callback) {
        get(String.format("https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s", corpID, secret),
                null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                access_token = response.optString("access_token");
                //System.out.println("token: " + access_token);
                if (callback != null) {
                    callback.run();
                }
            }
        });
    }

    interface Callback {
        void run();
    }

    static AsyncHttpClient client = new AsyncHttpClient();

    static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    static void post(String url, JSONObject params, AsyncHttpResponseHandler responseHandler) {
        StringEntity entity = new StringEntity(params.toString(), "utf-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.post(null, url, entity, "application/json", responseHandler);
    }

}

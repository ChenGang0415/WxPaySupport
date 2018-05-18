package com.lalameow.wxpaysupport.network.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lalameow.wxpaysupport.network.WxHttpRequest;
import com.lalameow.wxpaysupport.network.WxParmar;
import com.lalameow.wxpaysupport.network.WxRequestUtil;
import com.lalameow.wxpaysupport.exception.WxRequestException;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/17
 * 时间: 22:11
 * 功能：请进行修改
 */
public class HeartCheck implements WxHttpRequest {
    @Override
    public void requestdata() throws IOException, WxRequestException {
        if(WxParmar.SYNCKEY!=null) {
            CookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpGet httpGet = new HttpGet(buildUrl());
            httpGet.addHeader("Referer","https://"+WxParmar.DomainURL+"/");
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String content = EntityUtils.toString(httpEntity);
            content=content.substring(content.indexOf("=")+1,content.length());
            JsonParser parser=new JsonParser();
            JsonObject baseObj=parser.parse(content).getAsJsonObject();
            int retcode=baseObj.get("retcode").getAsInt();
            int selector=baseObj.get("selector").getAsInt();
            if(retcode == 1101 || retcode== 1100){
                throw new WxRequestException("心跳检测失败，错误代码："+retcode);
            }
            if(selector<=0){
//                throw new WxRequestException("心跳检测失败，访问数据未送达！");
            }
            response.close();
            httpclient.close();
        }else {
            throw new WxRequestException("心跳检测失败，不存在的SyncKey！");
        }
    }
    private String buildUrl(){
        try {
            StringBuilder builder=new StringBuilder();
            builder.append(WxParmar.PushDomainURL);
            builder.append("/cgi-bin/mmwebwx-bin/synccheck?r=").append(System.currentTimeMillis());
            builder.append("&skey=").append(URLEncoder.encode(WxParmar.SKEY,"UTF-8"));
            builder.append("&sid=").append(URLEncoder.encode(WxParmar.WXSID,"UTF-8"));
            builder.append("&uin=").append(URLEncoder.encode(WxParmar.WXUIN,"UTF-8"));
            builder.append("&deviceid=").append(WxRequestUtil.getDeviceid());
            builder.append("&synckey=").append(URLEncoder.encode(WxParmar.SYNCKEY,"UTF-8"));
            builder.append("&_=").append(System.currentTimeMillis());
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

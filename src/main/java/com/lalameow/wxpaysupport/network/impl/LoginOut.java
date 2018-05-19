package com.lalameow.wxpaysupport.network.impl;

import com.lalameow.wxpaysupport.WxPaySupport;
import com.lalameow.wxpaysupport.exception.WxRequestException;
import com.lalameow.wxpaysupport.network.WxHttpRequest;
import com.lalameow.wxpaysupport.network.WxParmar;
import com.lalameow.wxpaysupport.network.WxRequestUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 创建人: LaLaMeow-Chen
 * 日期: 2018/5/19
 * 时间: 13:11
 * 功能：请进行修改
 */
public class LoginOut implements WxHttpRequest {
    @Override
    public void requestdata() throws IOException, WxRequestException {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost(buildurl());
        List<NameValuePair> sumitData=new ArrayList<>();
        BasicNameValuePair dataOne=new BasicNameValuePair("sid",WxParmar.WXSID);
        BasicNameValuePair dataTwo=new BasicNameValuePair("uin",WxParmar.WXUIN);
        sumitData.add(dataOne);
        sumitData.add(dataTwo);
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(sumitData,"utf-8");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        WxPaySupport.plugin.getLogger().info("Web微信已经退出！");
        response.close();
        httpclient.close();
    }
    private String buildurl(){
        StringBuilder builder=new StringBuilder();
        builder.append("https://").append(WxParmar.DomainURL);
        builder.append("/cgi-bin/mmwebwx-bin/webwxlogout?redirect=1&type=0&skey=").append(WxParmar.SYNCKEY);
        return builder.toString();
    }
}

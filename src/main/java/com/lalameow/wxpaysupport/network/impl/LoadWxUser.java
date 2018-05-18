package com.lalameow.wxpaysupport.network.impl;

import com.google.gson.*;
import com.lalameow.wxpaysupport.network.WxRequestUtil;
import com.lalameow.wxpaysupport.network.WxHttpRequest;
import com.lalameow.wxpaysupport.network.WxParmar;
import com.lalameow.wxpaysupport.exception.WxRequestException;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/17
 * 时间: 22:21
 * 功能：请进行修改
 */
public class LoadWxUser implements WxHttpRequest{
    @Override
    public void requestdata() throws IOException, WxRequestException {
        if(WxParmar.SKEY!=null) {
            CookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpPost httpPost = new HttpPost(this.buildUrl());
            httpPost.setHeaders(WxRequestUtil.getLoadUserHeader());
            httpPost.setEntity(WxRequestUtil.postBaseData());
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            String resultJson = EntityUtils.toString(httpEntity,"utf-8");
            JsonParser jsonParser=new JsonParser();
            //JsonElement jsonElement=jsonParser.parse(new String(resultJson.getBytes("iso-8859-1"),"utf-8"));
            JsonElement jsonElement=jsonParser.parse(resultJson);
            JsonObject baseRoot=jsonElement.getAsJsonObject();
            JsonObject wxUser=baseRoot.get("User").getAsJsonObject();
            WxParmar.WxNickName=wxUser.get("NickName").getAsString();
            WxParmar.WxUserName=wxUser.get("UserName").getAsString();
            WxParmar.Signature=wxUser.get("Signature").getAsString();
            WxParmar.ClientVersion=baseRoot.get("ClientVersion").getAsString();
            WxRequestUtil.refreshSyncKey(baseRoot);
            response.close();
            httpclient.close();
        }else {
            throw new WxRequestException("用户未验证登录，不能读取用户信息！");
        }
    }
    private String buildUrl(){
        String builder = "https://" + WxParmar.DomainURL +
                "/cgi-bin/mmwebwx-bin/webwxinit?r=-1747024806&lang=zh_CN&pass_ticket=" + WxParmar.PASSTICKET;
        return builder;
    }

}

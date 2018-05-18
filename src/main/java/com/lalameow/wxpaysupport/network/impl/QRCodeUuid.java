package com.lalameow.wxpaysupport.network.impl;

import com.lalameow.wxpaysupport.network.WxHttpRequest;
import com.lalameow.wxpaysupport.network.WxParmar;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/17
 * 时间: 17:20
 * 功能：请进行修改
 */
public class QRCodeUuid implements WxHttpRequest {
    @Override
    public void requestdata() throws IOException {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpGet httpGet = new HttpGet(WxParmar.URL_getQrCodeUUID + System.currentTimeMillis());
        CloseableHttpResponse response = httpclient.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        String content = EntityUtils.toString(httpEntity);
        WxParmar.QRCodeUUID=content.substring(content.indexOf("\"")+1,content.lastIndexOf("\""));
        response.close();
        httpclient.close();
    }
}

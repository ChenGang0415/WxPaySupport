package com.lalameow.wxpaysupport.network.impl;

import com.lalameow.wxpaysupport.network.WxHttpRequest;
import com.lalameow.wxpaysupport.network.WxParmar;
import com.lalameow.wxpaysupport.exception.WxRequestException;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/17
 * 时间: 17:48
 * 功能：请进行修改
 */
public class WebWxLogin implements WxHttpRequest {
    @Override
    public void requestdata() throws IOException, WxRequestException {
        if(WxParmar.LOGINURL!=null && WxParmar.PushDomainURL!=null) {
            CookieStore cookieStore = new BasicCookieStore();
            RequestConfig config = RequestConfig.custom().setRedirectsEnabled(false).build();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(config).setDefaultCookieStore(cookieStore).build();
            HttpGet httpGet = new HttpGet(WxParmar.LOGINURL );
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String content = EntityUtils.toString(httpEntity);
            Document doc = Jsoup.parse(content);
            response.close();
            httpclient.close();
            String ret=doc.getElementsByTag("ret").text();
            if("0".equals(ret)){
                WxParmar.SKEY=doc.getElementsByTag("skey").text();
                WxParmar.WXSID=doc.getElementsByTag("wxsid").text();
                WxParmar.WXUIN=doc.getElementsByTag("wxuin").text();
                WxParmar.PASSTICKET=doc.getElementsByTag("pass_ticket").text();
                for (Cookie cookie : cookieStore.getCookies()) {
                    if(cookie.getName().equals("webwx_data_ticket")){
                        WxParmar.WEBWXDATATICKET=cookie.getValue();
                        break;
                    }
                }
            }else {
                String message=doc.getElementsByTag("message").text();
                throw new WxRequestException("登录失败！错误消息:"+message);
            }
        }else {
            throw new WxRequestException("登录未进行校验，请先校验登录！");
        }
    }

}

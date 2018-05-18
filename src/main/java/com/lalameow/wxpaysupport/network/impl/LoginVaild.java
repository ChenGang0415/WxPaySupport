package com.lalameow.wxpaysupport.network.impl;

import com.lalameow.wxpaysupport.WxPaySupport;
import com.lalameow.wxpaysupport.network.HttpServer;
import com.lalameow.wxpaysupport.network.WxHttpRequest;
import com.lalameow.wxpaysupport.network.WxParmar;
import com.lalameow.wxpaysupport.exception.WxRequestException;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/17
 * 时间: 17:30
 * 功能：请进行修改
 */
public class LoginVaild implements WxHttpRequest {
    @Override
    public void requestdata() throws IOException, WxRequestException {
        if(WxParmar.QRCodeUUID!=null) {
            String url= String.format(WxParmar.URL_loginWxWeb+System.currentTimeMillis(),WxParmar.QRCodeUUID);
            CookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpGet httpGet = new HttpGet( url );
            httpGet.addHeader(new BasicHeader("Referer","https://wx2.qq.com/"));
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String content = EntityUtils.toString(httpEntity);
            String[] resultData=content.split(";");
            int resultCode=0;
            if(resultData.length>0){
                String temp=resultData[0];
                String[] keyval=temp.split("=");
                resultCode=Integer.parseInt(keyval[1]);
            }
            if(resultCode==201){
                WxPaySupport.plugin.getLogger().info("扫码成功，请在手机点击确认！");
                response.close();
                httpclient.close();
                return;
            }
            if(!content.contains("\"")){
                response.close();
                httpclient.close();
                return;
            }
            WxParmar.LOGINURL=content.substring(content.indexOf("\"")+1,content.lastIndexOf("\""));
            WxParmar.DomainURL=WxParmar.LOGINURL.substring(WxParmar.LOGINURL.indexOf("//")+2,WxParmar.LOGINURL.indexOf("/cgi-bin"));
            if("wx.qq.com".equals(WxParmar.DomainURL)){
                WxParmar.PushDomainURL="https://webpush.weixin.qq.com";
            }else {
                WxParmar.PushDomainURL="https://webpush2.weixin.qq.com";
            }
            WxPaySupport.plugin.getLogger().info("登录连接为："+ WxParmar.LOGINURL);
            response.close();
            httpclient.close();
        }else {
            throw new WxRequestException("登录失败！");
        }
    }
}

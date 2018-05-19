package com.lalameow.wxpaysupport.network.impl;

import com.lalameow.wxpaysupport.WxPaySupport;
import com.lalameow.wxpaysupport.config.MainConfig;
import com.lalameow.wxpaysupport.network.HttpServer;
import com.lalameow.wxpaysupport.network.WxHttpRequest;
import com.lalameow.wxpaysupport.network.WxParmar;
import com.lalameow.wxpaysupport.exception.WxRequestException;
import com.lalameow.wxpaysupport.network.uitls.QRCodeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/17
 * 时间: 17:21
 * 功能：请进行修改
 */
public class QRCodeImge implements WxHttpRequest {

    private File saveQRCodePath;
    public QRCodeImge(File saveQRCodePath) {
        this.saveQRCodePath=saveQRCodePath;
        if(!saveQRCodePath.exists()){
            saveQRCodePath.mkdirs();
        }
    }

    @Override
    public void requestdata() throws IOException, WxRequestException {
        if(WxParmar.QRCodeUUID!=null) {
            CookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpGet httpGet = new HttpGet(WxParmar.URL_getQrCodeImge + WxParmar.QRCodeUUID);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            byte[] bytes = EntityUtils.toByteArray(httpEntity);
            if(MainConfig.enableQrSever){
                genQrImg(bytes);
            }else {
                String text=QRCodeUtils.decode(bytes);
                String qrstr=QRCodeUtils.generateQR(text,1,1);
                WxPaySupport.plugin.getLogger().info("\n"+qrstr);
            }
            response.close();
            httpclient.close();
        }else {
            throw new WxRequestException("二维码UUID不存在！");
        }
    }
    private void genQrImg(byte[] imgdata){
        new Thread(() -> {
            try {
                HttpServer.getInstans().listen(imgdata);
            } catch (IOException e) {
                WxPaySupport.plugin.getLogger().warning("HTTP服务器启动失败，请在插件目录扫码图片");
                File file=new File(saveQRCodePath,"wxLoginQr.png");
                try {
                    FileOutputStream fileOutputStream=new FileOutputStream(file);
                    fileOutputStream.write(imgdata);
                    fileOutputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }
}

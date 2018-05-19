package com.lalameow.wxpaysupport.network;

import com.lalameow.wxpaysupport.exception.WxRequestException;
import com.lalameow.wxpaysupport.network.impl.*;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/17
 * 时间: 16:53
 * 功能：请进行修改
 */
public class TestMain {
    public static void main(String[] args) throws IOException, WxRequestException, InterruptedException {
        QRCodeUuid qrCodeUuid=new QRCodeUuid();
        QRCodeImge qrCodeImge=new QRCodeImge(new File("H:\\hs\\WxPaySupport\\src\\main\\resources"));
        LoginVaild loginVaild=new LoginVaild();
        WebWxLogin loginWx=new WebWxLogin();
        LoadWxUser loadWxUser=new LoadWxUser();
        HeartCheck heartCheck=new HeartCheck();
        MoneyInfo moneyInfo=new MoneyInfo();
        qrCodeUuid.requestdata();
        qrCodeImge.requestdata();
        /*while (WxParmar.LOGINURL==null){
            try {
                loginVaild.requestdata();
                Thread.sleep(5000);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        loginWx.requestdata();
        loadWxUser.requestdata();
        while (true) {
            heartCheck.requestdata();
            moneyInfo.requestdata();
            Thread.sleep(10000);
        }*/
    }
}

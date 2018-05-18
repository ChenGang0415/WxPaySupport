package com.lalameow.wxpaysupport.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.http.entity.StringEntity;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/17
 * 时间: 16:57
 * 功能：请进行修改
 */
public class WxParmar {
    public static final String URL_getQrCodeUUID="https://login.wx2.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx2.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_=";
    public static final String URL_getQrCodeImge="https://login.weixin.qq.com/qrcode/";
    public static final String URL_loginWxWeb="https://login.wx2.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid=%s&tip=0&r=-1722694480&_=";
    public static String QRCodeUUID=null;
    public static String LOGINURL=null;
    public static String PushDomainURL=null;
    public static String DomainURL=null;
    public static String SKEY=null;
    public static String WXSID=null;
    public static String WXUIN=null;
    public static String PASSTICKET=null;
    public static String WEBWXDATATICKET=null;
    public static String SYNCKEY=null;
    public static String WxUserName=null;
    public static String WxNickName=null;
    public static String Signature=null;
    public static String ClientVersion=null;
    public static JsonElement syncKeyObj=null;

    public static void initParm(){
        QRCodeUUID=null;
        LOGINURL=null;
        PushDomainURL=null;
        DomainURL=null;
        SKEY=null;
        WXSID=null;
        WXUIN=null;
        PASSTICKET=null;
        WEBWXDATATICKET=null;
        SYNCKEY=null;
        WxUserName=null;
        WxNickName=null;
        Signature=null;
        ClientVersion=null;
        syncKeyObj=null;
    }
}

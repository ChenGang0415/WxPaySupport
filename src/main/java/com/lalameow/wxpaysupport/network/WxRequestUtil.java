package com.lalameow.wxpaysupport.network;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lalameow.wxpaysupport.pojo.RechargeRecord;
import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/17
 * 时间: 22:26
 * 功能：请进行修改
 *
 *
 */
public class WxRequestUtil {

    public static Header[] getLoadUserHeader(){
        List<Header> headers=new ArrayList<>();
        headers.add(new BasicHeader("Host","wx.qq.com"));
        headers.add(new BasicHeader("Connection","keep-alive"));
        headers.add(new BasicHeader("Accept","application/json, text/plain, */*"));
        headers.add(new BasicHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.63 Safari/537.36"));
        headers.add(new BasicHeader("Content-Type","application/json;charset=UTF-8"));
        headers.add(new BasicHeader("Referer","https://wx.qq.com/"));
        Header[] headerArray=new Header[headers.size()];
        headers.toArray(headerArray);
        return headerArray;

    }

    /**
     * 刷新synckey
     * @param baseRoot
     */
    public static void refreshSyncKey(JsonObject baseRoot){
        WxParmar.syncKeyObj=baseRoot.get("SyncKey");
        JsonObject jsonSyncKey=baseRoot.get("SyncKey").getAsJsonObject();
        JsonArray synckeyList=jsonSyncKey.get("List").getAsJsonArray();
        StringBuilder builder=new StringBuilder();
        for (JsonElement element : synckeyList) {
            JsonObject synckey=element.getAsJsonObject();
            String key=synckey.get("Key").getAsString();
            String val=synckey.get("Val").getAsString();
            builder.append(key).append("_").append(val).append("|");
        }
        WxParmar.SYNCKEY=builder.substring(0,builder.length()-1);
    }

    /**
     * 获取基础POS提交数据
     * @return
     */
    public static StringEntity postBaseData(){
        Gson gson=new Gson();
        JsonObject postdata=new JsonObject();
        postdata.addProperty("Uin",WxParmar.WXUIN);
        postdata.addProperty("Sid",WxParmar.WXSID);
        postdata.addProperty("Skey",WxParmar.SKEY);
        postdata.addProperty("DeviceID",WxRequestUtil.getDeviceid());
        JsonObject json=new JsonObject();
        json.add("BaseRequest",postdata);
        return new StringEntity(gson.toJson(json),"utf-8");
    }

    /**
     * 获取消息列表信息post数据
     * @return
     */
    public static StringEntity postMesageData(){
        Gson gson=new Gson();
        JsonObject postdata=new JsonObject();
        postdata.addProperty("Uin",WxParmar.WXUIN);
        postdata.addProperty("Sid",WxParmar.WXSID);
        postdata.addProperty("Skey",WxParmar.SKEY);
        postdata.addProperty("DeviceID",getDeviceid());
        JsonObject json=new JsonObject();
        json.add("BaseRequest",postdata);
        json.add("SyncKey",WxParmar.syncKeyObj);
        json.addProperty("rr",getRRData());
        return new StringEntity(gson.toJson(json),"utf-8");
    }

    /**
     * 获取15位随机数
     * @return
     */
    public static String getDeviceid(){
        String deviceid="e";
        Random random=new Random();
        for(int i=0;i<=15;i++){
            deviceid+= random.nextInt(10);
        }
        return deviceid;
    }

    /**
     * 获取信息列表的6位随机数
     * @return
     */
    private static String getRRData(){
        String rr="-1728";
        Random random=new Random();
        for(int i=0;i<=6;i++){
            rr+= random.nextInt(10);
        }
        return rr;
    }
}


package com.lalameow.wxpaysupport.network.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lalameow.wxpaysupport.WxPaySupport;
import com.lalameow.wxpaysupport.network.WxHttpRequest;
import com.lalameow.wxpaysupport.network.WxParmar;
import com.lalameow.wxpaysupport.network.WxRequestUtil;
import com.lalameow.wxpaysupport.exception.WxRequestException;
import com.lalameow.wxpaysupport.pojo.RechargeRecord;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bukkit.ChatColor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/18
 * 时间: 0:10
 * 功能：请进行修改
 */
public class MoneyInfo implements WxHttpRequest {
    @Override
    public void requestdata() throws IOException, WxRequestException {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost(this.buildUrl());
        httpPost.setEntity(WxRequestUtil.postMesageData());
        CloseableHttpResponse response = httpclient.execute(httpPost);
        HttpEntity httpEntity = response.getEntity();
        String resultJson= EntityUtils.toString(httpEntity);
        JsonParser jsonParser=new JsonParser();
        JsonElement jsonElement=jsonParser.parse(new String(resultJson.getBytes("iso-8859-1"),"utf-8"));
        JsonObject baseRoot=jsonElement.getAsJsonObject();
        WxRequestUtil.refreshSyncKey(baseRoot);
        JsonArray msgInfoArray=baseRoot.get("AddMsgList").getAsJsonArray();
        for (JsonElement element : msgInfoArray) {
            JsonObject msgObj=element.getAsJsonObject();
            String filename=msgObj.get("FileName").getAsString();
            int msgType=msgObj.get("MsgType").getAsInt();
            if(msgType==49 && (filename.contains("微信支付收款")|| filename.contains("二维码赞赏到账"))){
                String content=msgObj.get("Content").getAsString();
                content=content.replace("&lt;","<");
                content=content.replace("&gt;",">");
                RechargeRecord rechargeRecord=parsing(content,filename.contains("二维码赞赏到账"));
                WxPaySupport.rechargeRecordQueue.add(rechargeRecord);
                if(rechargeRecord.getPlayerName()==null){
                    WxPaySupport.plugin.getLogger().warning(rechargeRecord.toString()+"-此比充值未检测到填写的角色名称，请注意！");
                }else {
                    WxPaySupport.plugin.getLogger().info("有一笔新的充值:"+rechargeRecord.toString());
                }

            }
        }

    }
    private String buildUrl(){
        try {
            StringBuilder builder=new StringBuilder();
            builder.append("https://").append(WxParmar.DomainURL);
            builder.append("/cgi-bin/mmwebwx-bin/webwxsync?sid=").append(URLEncoder.encode(WxParmar.WXSID,"UTF-8"));
            builder.append("&pass_ticket=").append(URLEncoder.encode(WxParmar.PASSTICKET,"UTF-8"));
            builder.append("&deviceid=").append(WxRequestUtil.getDeviceid());
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析收款记录
     * @param content
     * @param isZanShang
     * @return
     */
    private RechargeRecord parsing(String content,boolean isZanShang){
        Document doc = Jsoup.parse(content);
        String money=doc.clone().select("line_content > topline > value > word").text();
        List<String> textList=doc.clone().select("line_content > lines > line > value > word").eachText();
        List<String> keyList=doc.clone().select("line_content > lines > line > key > word").eachText();
        RechargeRecord rechargeRecord=new RechargeRecord();
        rechargeRecord.setMoney(Double.parseDouble(money.substring(1,money.length())));
        if(isZanShang){
            int fkfindex=keyList.indexOf("付款方留言");
            int len=textList.size();
            if(fkfindex>=0){
                rechargeRecord.setPlayerName(textList.get(fkfindex));
            }
            rechargeRecord.setDes(textList.get(len-1));

        }else {
            if(textList.size()<3){
                rechargeRecord.setDes(textList.get(0));
            }else {
                rechargeRecord.setPlayerName(textList.get(0));
                rechargeRecord.setDes(textList.get(1));
            }
        }
        return rechargeRecord;
    }
}

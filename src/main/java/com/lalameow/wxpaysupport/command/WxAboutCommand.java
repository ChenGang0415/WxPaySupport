package com.lalameow.wxpaysupport.command;

import com.lalameow.wxpaysupport.WxPaySupport;
import com.lalameow.wxpaysupport.exception.WxRequestException;
import com.lalameow.wxpaysupport.network.HttpServer;
import com.lalameow.wxpaysupport.network.WxParmar;
import com.lalameow.wxpaysupport.wxsetup.WebWxStarter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import javax.jws.WebParam;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/18
 * 时间: 12:46
 * 功能：请进行修改
 */
public class WxAboutCommand extends AbstractCommand {
    @Override
    protected void init() {
        this.command="wxs";
        this.commands.put("wxlg","扫描二维码登录收款微信");
    }

    @Override
    protected void showHelp() {
        String msg= ChatColor.GREEN+"_____________["+ChatColor.RED+"微信充值]"+ChatColor.GREEN+"_______________\n";
        for (Map.Entry<String, String> stringStringEntry : commands.entrySet()) {
            msg+=ChatColor.RED+"[微信充值] "+ ChatColor.AQUA+"/"+command+" "+stringStringEntry.getKey()+"\n";
            msg+=ChatColor.RED+"[微信充值] "+ ChatColor.DARK_GREEN+stringStringEntry.getValue()+"\n";
        }
        msg+=ChatColor.GREEN+"_____________["+ChatColor.RED+"微信充值]"+ChatColor.GREEN+"_______________\n";
        this.sender.sendMessage(msg);
    }

    public void wxlgCmd(){
        WxParmar.initParm();
        WxPaySupport.plugin.getLogger().info("初始化所有登录参数");
        Bukkit.getServer().getScheduler().runTaskAsynchronously(WxPaySupport.plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    WebWxStarter.QRUUIDGEN.getWxHttpRequest().requestdata();
                    LaLaMeowMessage.sendSuccess(sender,"获取二维码UUID成功，即将生成二维码图片");
                    WebWxStarter.QRIMGEGEN.getWxHttpRequest().requestdata();
                    LaLaMeowMessage.sendSuccess(sender,"二维码图片生成成功，请用收款微信进行扫码登录");
                    int timeoutCount=0;
                    while (WxParmar.LOGINURL==null){
                        if(timeoutCount>6){
                            WxParmar.initParm();
                            //超时关闭webserver
                            HttpServer.getInstans().stoplisten();
                            throw new WxRequestException("验证超时，请重新输入命令");
                        }
                        LaLaMeowMessage.sendSuccess(sender,"等待扫码");
                        WebWxStarter.CHECKLOGIN.getWxHttpRequest().requestdata();
                        Thread.sleep(10000);
                        timeoutCount++;

                    }
                    //成功后关闭webserver
                    HttpServer.getInstans().stoplisten();
                    LaLaMeowMessage.sendSuccess(sender,"开始登录！");
                    WebWxStarter.WEBWXLOGIN.getWxHttpRequest().requestdata();
                    LaLaMeowMessage.sendSuccess(sender,"登录成功！初始化收款微信账户参数！");
                    WebWxStarter.LOADUSER.getWxHttpRequest().requestdata();
                    LaLaMeowMessage.sendSuccess(sender,"参数初始化成功.登录人:"+ WxParmar.WxNickName);
                    LaLaMeowMessage.sendSuccess(sender,"启动微信收款监听线程！");
                    while (true){
                        try {
                            WebWxStarter.HEAARCHECK.getWxHttpRequest().requestdata();
                        }catch (WxRequestException e){
                          //  WxPaySupport.plugin.getLogger().warning(e.getMessage());
                        }
                        WebWxStarter.MONEYINFOLIS.getWxHttpRequest().requestdata();
                        Thread.sleep(10000);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    LaLaMeowMessage.sendError(sender,"访问微信接口异常！");
                } catch (WxRequestException e) {
                    LaLaMeowMessage.sendError(sender,e.getMessage());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

package com.lalameow.wxpaysupport;

import com.lalameow.wxpaysupport.command.WxAboutCommand;
import com.lalameow.wxpaysupport.config.MainConfig;
import com.lalameow.wxpaysupport.log.RechargeLog;
import com.lalameow.wxpaysupport.log.filelog.RechargeFileLog;
import com.lalameow.wxpaysupport.pojo.RechargeRecord;
import com.lalameow.wxpaysupport.wxsetup.RechargeQueueHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.LinkedBlockingQueue;

public final class WxPaySupport extends JavaPlugin {
    public static LinkedBlockingQueue<RechargeRecord> rechargeRecordQueue=new LinkedBlockingQueue<>();
    public static WxPaySupport plugin;
    public static RechargeLog rechargeLog;
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin=this;
        getCommand("wxs").setExecutor(new WxAboutCommand());
        MainConfig mainConfig=new MainConfig();
        Bukkit.getServer().getScheduler().runTaskAsynchronously(this,new RechargeQueueHandler());
        if(MainConfig.useMysql){
            //todo 未实现mysql日志存储
            rechargeLog=new RechargeFileLog();
        }else {
            rechargeLog=new RechargeFileLog();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

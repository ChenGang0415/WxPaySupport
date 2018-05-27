package com.lalameow.wxpaysupport.wxsetup;

import com.lalameow.wxpaysupport.WxPaySupport;
import com.lalameow.wxpaysupport.config.MainConfig;
import com.lalameow.wxpaysupport.pojo.RechargeRecord;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 创建人: LaLaMeow-Chen
 * 日期: 2018/5/18
 * 时间: 17:55
 * 功能：请进行修改
 */
public class RechargeQueueHandler implements Runnable {
    @Override
    public void run() {
        WxPaySupport.plugin.getLogger().info(ChatColor.GREEN+"充值成功监控线程启动");
        while (true){
            try {
                RechargeRecord rechargeRecord=WxPaySupport.rechargeRecordQueue.take();
                if(rechargeRecord!=null){
                    if(rechargeRecord.getPlayerName()!=null){
                        List<String> cmds=MainConfig.czOkCmd;
                        int yxb=(int)(MainConfig.czbl*rechargeRecord.getMoney());
                        for (String cmd : cmds) {
                            String finalCmd=cmd.replace("%cz.playerName%",rechargeRecord.getPlayerName())
                                    .replace("%cz.money%",rechargeRecord.getMoney()+"")
                                    .replace("%cz.yxb%",yxb+"");
                            Bukkit.getServer().getScheduler().runTask(WxPaySupport.plugin, new Runnable() {
                                @Override
                                public void run() {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
                                }
                            });
                        }
                    }
                    //todo 存储充值日志
                    WxPaySupport.rechargeLog.saveLog(rechargeRecord);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

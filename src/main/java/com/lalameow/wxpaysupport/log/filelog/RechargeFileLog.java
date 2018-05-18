package com.lalameow.wxpaysupport.log.filelog;

import com.lalameow.wxpaysupport.WxPaySupport;
import com.lalameow.wxpaysupport.log.RechargeLog;
import com.lalameow.wxpaysupport.pojo.RechargeRecord;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * 创建人: LaLaMeow-Chen
 * 日期: 2018/5/18
 * 时间: 18:26
 * 功能：请进行修改
 */
public class RechargeFileLog implements RechargeLog {

    private File file;
    public RechargeFileLog(){
        file=new File(WxPaySupport.plugin.getDataFolder(),"rechargeLog.yml");
        if(!file.exists()){
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
               WxPaySupport.plugin.getLogger().warning("无法创建日志文件");
               file=null;
            }
        }
    }

    @Override
    public void saveLog(RechargeRecord rechargeRecord) {
        FileLogSaveObj.saveRecord(rechargeRecord,file);
    }


}

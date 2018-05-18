package com.lalameow.wxpaysupport.log.filelog;

import com.lalameow.wxpaysupport.pojo.RechargeRecord;
import lombok.Data;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * 创建人: LaLaMeow-Chen
 * 日期: 2018/5/18
 * 时间: 18:38
 * 功能：请进行修改
 */
@Data
public class FileLogSaveObj {
    private String playerName="WX充值的未知用户";
    private double sum=0;
    private List<String> record=new ArrayList<>();

    private static FileLogSaveObj findPlayer(String playerName, YamlConfiguration logfileYml) throws IOException, InvalidConfigurationException {
        double sumMoney=logfileYml.getDouble(playerName+".sum");
        List<String> recordList=(List<String>)logfileYml.getList(playerName+".record");
        if(recordList!=null && sumMoney!=0){
            FileLogSaveObj fileLogSaveObj=new FileLogSaveObj();
            fileLogSaveObj.setPlayerName(playerName);
            fileLogSaveObj.setRecord(recordList);
            fileLogSaveObj.setSum(sumMoney);
            return fileLogSaveObj;
        }
        return null;
    }
    public static void saveRecord(RechargeRecord rechargeRecord,File logFile){
        if(rechargeRecord!=null && rechargeRecord.getPlayerName()!=null){
            try {
                YamlConfiguration logfileYml=new YamlConfiguration();
                logfileYml.load(logFile);
                FileLogSaveObj fileLogSaveObj=findPlayer(rechargeRecord.getPlayerName(),logfileYml);
                if(fileLogSaveObj!=null){
                    fileLogSaveObj.setSum(fileLogSaveObj.getSum()+rechargeRecord.getMoney());
                    fileLogSaveObj.getRecord().add(rechargeRecord.toString());
                    logfileYml.set(fileLogSaveObj.getPlayerName()+".sum",fileLogSaveObj.getSum());
                    logfileYml.save(logFile);
                }else {
                    FileLogSaveObj newlog=new FileLogSaveObj();
                    newlog.setPlayerName(rechargeRecord.getPlayerName());
                    newlog.setSum(rechargeRecord.getMoney());
                    newlog.getRecord().add(rechargeRecord.toString());
                    logfileYml.options().copyDefaults(true);
                    logfileYml.addDefault(newlog.getPlayerName()+".sum",newlog.getSum());
                    logfileYml.addDefault(newlog.getPlayerName()+".record",newlog.getRecord());
                    logfileYml.save(logFile);
                }
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
}

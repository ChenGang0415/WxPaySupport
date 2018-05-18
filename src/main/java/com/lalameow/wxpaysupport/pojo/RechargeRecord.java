package com.lalameow.wxpaysupport.pojo;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/18
 * 时间: 1:45
 * 功能：充值记录
 */
@Data
public class RechargeRecord {
    private String playerName;
    private Double money;
    private String des;
    private Date createDate=new Date();

    @Override
    public String toString() {
        String name=this.playerName==null?"WX充值的未知用户":this.playerName;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(this.createDate);
        return "玩家"+name+"在"+dateString+"充值了"+this.money+"元-"+this.des;
    }
}

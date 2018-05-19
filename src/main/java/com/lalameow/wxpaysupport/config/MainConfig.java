package com.lalameow.wxpaysupport.config;

import com.lalameow.wxpaysupport.WxPaySupport;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * 创建人: LaLaMeow-Chen
 * 日期: 2018/5/18
 * 时间: 17:20
 * 功能：请进行修改
 */
public class MainConfig extends AbstractConfig{


    public static String  verstion;
    public static boolean useMysql;
    public static String  mysqlIp;
    public static int     mysqlPort=3306;
    public static String  mysqlUser;
    public static String  mysqlPwd;
    public static String  mysqlDB;
    public static String  msyqlTbPre;
    public static int     httpserverport=8080;
    public static boolean enableModUi;
    public static String  czImgUrl;
    public static int     czbl;
    public static String  czOkCmd;
    public static boolean enableQrSever;


    public MainConfig() {
        super("config.yml", null, WxPaySupport.plugin);
    }

    @Override
    public void createDefault() {
        WxPaySupport.plugin.getLogger().info("创建主配置文件");
        try {
            this.getConfigFile().load(new InputStreamReader(WxPaySupport.plugin.getResource(this.getFileName()), "UTF-8"));
            this.save();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            WxPaySupport.plugin.getLogger().info("主配置文件创建失败");
        }
    }

    @Override
    public void initConfig() {
        verstion=this.getConfigFile().getString("verstion");
        useMysql=this.getConfigFile().getBoolean("useMysql");
        mysqlIp=this.getConfigFile().getString("mysql.ip");
        mysqlPort=this.getConfigFile().getInt("mysql.port");
        mysqlUser=this.getConfigFile().getString("mysql.user");
        mysqlPwd=this.getConfigFile().getString("mysql.pwd");
        mysqlDB=this.getConfigFile().getString("mysql.db");
        msyqlTbPre=this.getConfigFile().getString("mysql.tbpre");
        httpserverport=this.getConfigFile().getInt("httpserverport");
        enableModUi=this.getConfigFile().getBoolean("enableModUi");
        czImgUrl=this.getConfigFile().getString("czImgUrl");
        czbl=this.getConfigFile().getInt("czbl");
        czOkCmd=this.getConfigFile().getString("czOkCmd");
        enableQrSever=this.getConfigFile().getBoolean("enableQrSever");
    }
}

package com.lalameow.wxpaysupport.wxsetup;

import com.lalameow.wxpaysupport.WxPaySupport;
import com.lalameow.wxpaysupport.network.WxHttpRequest;
import com.lalameow.wxpaysupport.network.impl.*;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/18
 * 时间: 2:46
 * 功能：请进行修改
 */
public enum WebWxStarter {
    QRUUIDGEN(new QRCodeUuid()),
    QRIMGEGEN(new QRCodeImge(WxPaySupport.plugin.getDataFolder())),
    CHECKLOGIN(new LoginVaild()),
    WEBWXLOGIN(new WebWxLogin()),
    LOADUSER(new LoadWxUser()),
    HEAARCHECK(new HeartCheck()),
    MONEYINFOLIS(new MoneyInfo());
    @Getter
    private WxHttpRequest wxHttpRequest;

    WebWxStarter(WxHttpRequest wxHttpRequest) {
        this.wxHttpRequest = wxHttpRequest;
    }
}

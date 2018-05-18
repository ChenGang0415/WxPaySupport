package com.lalameow.wxpaysupport.network;

import com.lalameow.wxpaysupport.exception.WxRequestException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2018/5/17
 * 时间: 16:09
 * 功能：请进行修改
 */
public interface WxHttpRequest {


    /**
     * 发送数据
     * @throws IOException
     * @throws WxRequestException
     */
    void requestdata() throws IOException, WxRequestException;

}

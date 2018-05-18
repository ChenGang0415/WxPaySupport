package com.lalameow.wxpaysupport.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2017/3/1
 * 时间: 1:44
 * 功能：发送消息工具类
 */
public class LaLaMeowMessage {
    public static void sendError(CommandSender sender, String message) {
        if (sender == null) {
            return;
        }
        sender.sendMessage(ChatColor.RED + message);
    }

    public static void sendSuccess(CommandSender sender, String message) {
        if (sender == null) {
            return;
        }
        sender.sendMessage(ChatColor.GREEN + message);
    }
}

package com.lalameow.wxpaysupport.command;

import com.lalameow.wxpaysupport.exception.LaLaMeowException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2017/2/28
 * 时间: 23:51
 * 功能：请进行修改
 */
public abstract class AbstractCommand implements CommandExecutor {

    protected String command;
    protected HashMap<String, String> commands = new HashMap<String, String>();
    protected CommandSender sender;
    protected String[] args;
    protected abstract void init();
    protected abstract void showHelp();


    private void checkPermission() throws LaLaMeowException {
        if(sender instanceof Player){
            String permission="";
            if(!sender.hasPermission(command)){
                throw new LaLaMeowException("你没有权限使用此命令！");
            }
            for (String cmd : commands.keySet()) {
                if(!sender.hasPermission(command+"."+cmd)){
                    throw new LaLaMeowException("你没有权限使用此命令！");
                }
            }
        }
    }



    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        init();
        this.sender=sender;
        this.args=args;
        try {
            checkPermission();
        } catch (LaLaMeowException e) {
            LaLaMeowMessage.sendError(sender,e.getMessage());
            return false;

        }
        if (args.length==0 || args[0].equalsIgnoreCase("help")) {
            showHelp();
            return true;
        }
        for (String c : commands.keySet()) {
            String cmd=c.split(" ")[0];
            if (cmd.equalsIgnoreCase(args[0])) {
                try {
                    Method method  = this.getClass().getMethod(args[0].toLowerCase() + "Cmd");
                    method.invoke(this);
                    return true;
                }catch (InvocationTargetException e) {
                    if (e.getCause() instanceof LaLaMeowException) {
                        LaLaMeowMessage.sendError(sender, e.getCause().getMessage());
                    } else {
                        e.getCause().printStackTrace();
                        LaLaMeowMessage.sendError(sender, "无效的命令");
                    }
                } catch (NoSuchMethodException | IllegalAccessException e) {
                    e.printStackTrace();
                    LaLaMeowMessage.sendError(sender, "无效的命令");
                }

            }
        }
        LaLaMeowMessage.sendError(sender, "命令不存在，请确认后再次输入！");
        return false;
    }
}

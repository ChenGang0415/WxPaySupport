package com.lalameow.wxpaysupport.config;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * 创建人: 陈刚
 * 日期: 2017/2/28
 * 时间: 18:19
 * 功能：配置文件
 */
public abstract class AbstractConfig {

    @Getter
    @Setter
    private String fileName;//文件名字
    @Getter
    @Setter
    private YamlConfiguration configFile;
    private Class entityType;
    @Getter
    private File file;
    protected JavaPlugin plugin;

    protected AbstractConfig(String fileName, Class entityType, JavaPlugin plugin) {
        this.entityType = entityType;
        this.fileName = fileName;
        this.plugin = plugin;
        this.setConfigFile(new YamlConfiguration());
        loadFile();
    }

    /**
     * 创建默认信息
     */
    public abstract void createDefault();

    /**
     * 初始化变量的时候使用，如果不需要初始化可以不实现
     */
    public abstract void initConfig();

    /**
     * 读取并创建文件
     */
    private void loadFile() {
        try {
            this.file = new File(this.plugin.getDataFolder(), this.getFileName());
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                this.createDefault();
                this.initConfig();
            } else {
                this.getConfigFile().load(file);
                this.initConfig();
            }
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存配置文件
     */
    public void save() {
        try {
            if (file == null) {
                this.plugin.getLogger().info(ChatColor.RED + "配置文件" + fileName + "异常!");
                return;
            }
            Files.createParentDirs(file);
            String data = configFile.saveToString();
            if (entityType != null) {
                data = data.replace("!!" + entityType.getName(), "");
            }
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
            try {
                writer.write(data);
            } finally {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            this.plugin.getLogger().info(ChatColor.RED + "保存配置文件" + fileName + "保存错误");
        }
    }

    /**
     * 重新读取配置文件
     */
    public void reload() {
        try {
            configFile = new YamlConfiguration();
            configFile.load(file);
            initConfig();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            this.plugin.getLogger().info(ChatColor.RED + "配置文件" + fileName + "读取错误");
        }
    }

    public Object get(String path) throws IllegalAccessException {
        if (this.configFile.contains(path))
            return this.configFile.get(path);
        else throw new IllegalAccessException("配置项不存在！");
    }

    /**
     * 获取配置文件读取路径
     *
     * @param rootName
     * @param attr
     * @return
     */
    public String getPath(String rootName, String... attr) {
        StringBuilder configPath = new StringBuilder(rootName);
        for (String s : attr) {
            configPath.append(".").append(s);
        }
        return configPath.toString();
    }

    /**
     * 从jar资源文件创建默认配置
     */
    public void createFromResource() {
        try {
            this.getConfigFile().load(new InputStreamReader(plugin.getResource(this.getFileName()), "UTF-8"));
            this.save();
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            plugin.getLogger().warning(this.getFileName() + " created is failed. ");
        }
    }
}

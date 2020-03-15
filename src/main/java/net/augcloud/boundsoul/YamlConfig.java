package net.augcloud.boundsoul;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 *  SetFiles class
 *
 * @author Arisa
 * @date 2016/10/31
 */
public class YamlConfig {
    private static YamlConfiguration Settings = null;
    
    static void regConfig() throws IOException {
        File file = new File(BoundSoul.plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            BoundSoul.plugin.saveResource("config.yml", true);
        }
        file = new File(BoundSoul.plugin.getDataFolder(), "config.yml");
        Settings = FileConfig.loadConfiguration(file);
        if (!"1.3".equals(Settings.getString("Version"))) {
            BoundSoul.plugin.getLogger().info("错误，你使用的配置不符合当前插件");
            BoundSoul.plugin.getLogger().info("插件会备份后重新生成一份新的配置");
            Settings.save(new File(BoundSoul.plugin.getDataFolder(), "config.yml.old"));
            boolean a = file.delete();
            BoundSoul.plugin.saveResource("config.yml", true);
            file = new File(BoundSoul.plugin.getDataFolder(), "config.yml");
            Settings = FileConfig.loadConfiguration(file);
        }
        
        
       
    }
    
    public static ConfigurationSection getLang() {
        return Settings.getConfigurationSection("Lang");
    }
    
    public static YamlConfiguration getConfig() {
        return Settings;
    }
    

   public static String getPluginPrefix() {
        return getConfig().getString("Plugin_Prefix").replaceAll("&", "§");
    }
    
}

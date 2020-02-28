package net.augcloud.boundsoul;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 *  SetFiles class
 *
 * @author Arisa
 * @date 2016/10/31
 */
class YamlConfig {
    private static YamlConfiguration Settings = null;
    
    static void regConfig() {
        File file = new File(Main.plugin.getDataFolder(), "config.yml");
        if (!file.exists()) {
            Main.plugin.saveResource("config.yml", true);
        }
        file = new File(Main.plugin.getDataFolder(), "config.yml");
        Settings = FileConfig.loadConfiguration(file);
        if (!"1.0".equals(Settings.getString("Version"))) {
            Main.plugin.getLogger().info("Error,your plugin version isn't this config");
            Main.plugin.onDisable();
        }
        Main.censor_freq = getConfig().getInt("Censor-freq");
    }
    
    static YamlConfiguration getConfig() {
        return Settings;
    }
    

    static String getPlugin_Prefix() {
        return getConfig().getString("Plugin_Prefix").replaceAll("&", "§");
    }
}

package net.augcloud.boundsoul;

import net.augcloud.boundsoul.core.IllegalPlayer;
import net.augcloud.boundsoul.core.PermitInventory;
import net.augcloud.boundsoul.core.ThreadManager;
import net.augcloud.boundsoul.events.ToolOfEvents;
import net.augcloud.boundsoul.storage.UtilsOfStorage;
import net.augcloud.boundsoul.storage.sqlite.UtilsOfSQLite;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.swing.*;
import java.io.IOException;


/**
 *  Main class
 *
 * @author Arisa
 * @date 2016/10/31
 */
public class BoundSoul extends JavaPlugin implements Listener {
    public static Plugin plugin = null;
    public static final boolean debug = false;
    public static final IllegalPlayer illegalPlayer = new IllegalPlayer();
    public static final ThreadManager threadManager = new ThreadManager();
    
    
    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Listener) this);
        threadManager.getBoundThread().allStop();
        getLogger().info("Plugin disable now!");
        getLogger().info("插件定制/Plug-in customization");
        getLogger().info("服务器租用、托管/Server Rent、Trusteeship");
        getLogger().info("联系QQ/Tell me QQ-1131271403 Arisa");
    }
    
    @Override
    public void onEnable() {
        long startTime = System.nanoTime();
        if (!getDataFolder().exists()) {
          boolean a = getDataFolder().mkdir();
          if(!a){
              getLogger().info("文件夹未创建，插件卸载");
              this.getServer().getPluginManager().disablePlugin(this);
          }
        }
        plugin = this;
        try {
            YamlConfig.regConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        threadManager.instantiation();
        ToolOfEvents.inits();
        PermitInventory.inits();
        getCommand("BoundSoul").setExecutor(new Commands());
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            // Failed to submit the stats :-(
        }
        UtilsOfSQLite.init();
        getLogger().info("Plugin Enable now!");
        getLogger().info("插件定制/Plug-in customization");
        getLogger().info("服务器租用、托管/Server Rent、Trusteeship");
        getLogger().info("联系QQ/Tell me QQ-1131271403 Arisa");
        long endTime = System.nanoTime();
        getLogger().info("Loading completed!Used " + ((endTime - startTime) / 1000000L) + "ms");
    }
    
    
}

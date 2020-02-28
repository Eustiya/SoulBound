package net.augcloud.boundsoul;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;


/**
 *  Main class
 *
 * @author Arisa
 * @date 2016/10/31
 */
public class Main extends JavaPlugin implements Listener {
    static Plugin plugin = null;
    static final List<BoundManager> bs = new ArrayList();
    static BoundThread BoundThread = null;
    public static final boolean debug = false;
    static int censor_freq = 500;
    static final IllegalPlayer illegalPlayer = new IllegalPlayer();
    
    private static void updataPlayerinv() {
        List<World> worlds = Bukkit.getWorlds();
        if (worlds == null || worlds.isEmpty()) {
            return;
        }
        for (int i = worlds.size() - 1; i >= 0; i--) {
            List<Player> players = worlds.get(i).getPlayers();
            if (players != null && !players.isEmpty()) {
                for (int j = players.size() - 1; j >= 0; j--) {
                    Player player = players.get(j);
                    PlayerInventory inv = player.getInventory();
                    if (inv != null) {
                        bs.add(new BoundManager(player, inv));
                    }
                }
            }
        }
    }
    
    public static boolean isNumber(String str) {
        return str.matches("^[0-9]*$");
    }
    
    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Listener) this);
        BoundThread.allStop();
        getLogger().info("Plugin disable now!");
        getLogger().info("插件定制/Plug-in customization");
        getLogger().info("服务器租用、托管/Server Rent、Trusteeship");
        getLogger().info("联系QQ/Tell me QQ-1131271403 Arisa");
    }
    
    @Override
    public void onEnable() {
        long startTime = System.nanoTime();
        if (!getDataFolder().exists()) {
          getDataFolder().mkdir();
        }
        plugin = this;
        YamlConfig.regConfig();
        EventListener.autoBindLore = EventListener.getLowerRe(YamlConfig.getConfig().getString("AutoBindLore_Before"));
        EventListener.autoBindLore_later = EventListener.getLowerRe(YamlConfig.getConfig().getString("AutoBindLore_later"));
        censor_freq = YamlConfig.getConfig().getInt("Censor-freq");
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);
        getCommand("BoundSoul").setExecutor(new MainCmd());
        Instantiation();
        getLogger().info("Plugin Enable now!");
        getLogger().info("插件定制/Plug-in customization");
        getLogger().info("服务器租用、托管/Server Rent、Trusteeship");
        getLogger().info("联系QQ/Tell me QQ-1131271403 Arisa");
        long endTime = System.nanoTime();
        getLogger().info("Loading completed!Used " + ((endTime - startTime) / 1000000L) + "ms");
    }
    
    private void Instantiation() {
        BoundThread = new BoundThread();
        BoundThread.init();
    }
    
    public static class BoundThread {
        Thread updataInvThread;
        
        Thread checkThread;
        
        boolean threadRunning = false;
        
        void init() {
            if (!this.threadRunning) {
                this.threadRunning = true;
                startThread();
                checkThread();
                return;
            }
            allStart();
        }
        
        void allStart() {
            this.updataInvThread.start();
            this.checkThread.start();
        }
        
        void allStop() {
            this.threadRunning = false;
            this.updataInvThread.stop();
            this.checkThread.stop();
        }
        
        void startThread() {
            Runnable runnable = new Runnable() {
                boolean running = true;
                
                @Override
                public void run() {
                    while (Main.BoundThread.this.threadRunning && this.running) {
                        if (Main.bs != null && !Main.bs.isEmpty()) {
                            Main.bs.clear();
                        }
                        Main.updataPlayerinv();
                        EventListener.updataInv(Main.bs);
                        try {
                            Thread.sleep(Main.censor_freq);
                        } catch (InterruptedException e) {
                            this.running = false;
                            Main.BoundThread.this.updataInvThread.stop();
                            Main.BoundThread.this.startThread();
                            Main.plugin.getLogger()
                                    .info("警告!插件异步线程运行中发生错误崩溃，检测绑定功能已经失效!!请截图完整错误报告内容并申报至QQ1131271403Arisa进行修复，正在重启线程");
                            e.printStackTrace();
                        }
                    }
                }
            };
            this.updataInvThread = new Thread(runnable, "Updata-Thread");
            this.updataInvThread.start();
            Main.plugin.getLogger().info("线程运行中");
        }
        
        void checkThread() {
            Runnable runnable = new Runnable() {
                boolean running = true;
                
                @Override
                public void run() {
                    while (Main.BoundThread.this.threadRunning && this.running) {
                        try {
                            if (!Main.BoundThread.this.updataInvThread.isAlive()) {
                                Main.BoundThread.this.updataInvThread.stop();
                                Main.BoundThread.this.startThread();
                                Main.plugin.getLogger().info("发现检测线程不运行，自动激活了它");
                            }
                            Main.illegalPlayer.ruin();
                            Thread.sleep(20000L);
                        } catch (InterruptedException e) {
                            Main.plugin.getLogger().info("检测线程崩溃");
                            this.running = false;
                            Main.BoundThread.this.checkThread.stop();
                            Main.BoundThread.this.checkThread();
                            e.printStackTrace();
                        }
                    }
                }
            };
            this.checkThread = new Thread(runnable, "Check-Thread");
            this.checkThread.start();
            Main.plugin.getLogger().info("检测线程运行，此线程会每10秒确认一次主线程存活，如果停止会自动重启");
        }
    }
}

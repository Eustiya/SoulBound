/*
 * ?2021 August-soft Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.augcloud.boundsoul.events;

import net.augcloud.boundsoul.BoundSoul;
import net.augcloud.boundsoul.PluginData;
import net.augcloud.boundsoul.YamlConfig;
import net.augcloud.boundsoul.core.BoundManager;
import net.augcloud.boundsoul.utils.Utils;
import net.augcloud.boundsoul.utils.UtilsOfEffect;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.List;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/1 18:50
 * @description：
 * @version: $
 */
public class ToolOfEvents {
    
    public static String autoBindLore;
    private static String autoBindLore_later;
    public static String illegalItems;
    private static boolean throwOutItem = false;
    
    public static void inits(){
        autoBindLore = Utils.getLowerAndReplace(YamlConfig.getConfig().getString("AutoBindLore_Before"));
        autoBindLore_later = Utils.getLowerAndReplace(YamlConfig.getConfig().getString("AutoBindLore_later"));
        illegalItems = Utils.getLowerAndReplace(YamlConfig.getConfig().getString("IllegalItems"));
        throwOutItem = YamlConfig.getConfig().getBoolean("throwOutItem");
        if(!YamlConfig.getConfig().getBoolean("allowThrowItem")){
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerDropItemListener(), BoundSoul.plugin);
        }
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryOpenListener(), BoundSoul.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerPickupItemListener(), BoundSoul.plugin);;
        Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), BoundSoul.plugin);
        if(YamlConfig.getConfig().getBoolean("useInteraction")){
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), BoundSoul.plugin);
        }
        if(YamlConfig.getConfig().getBoolean("useBindNotDrop")){
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(), BoundSoul.plugin);;
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), BoundSoul.plugin);
            Bukkit.getServer().getPluginManager().registerEvents(new PlayerLoginListener(), BoundSoul.plugin);
    
        }
    }
    
    public static void updataPlayerinv() {
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
                        PluginData.bs.add(new BoundManager(player, inv));
                    }
                }
            }
        }
    }
    
    public static void updataInventory(List<BoundManager> bs) {
        if (bs == null || bs.isEmpty()) {
            return;
        }
        for (BoundManager b : bs) {
            PlayerInventory inv = b.getInv();
            Iterator<ItemStack> iter = inv.iterator();
            final Player player = b.getPlayer();
            if (player.isOp()) {
                continue;
            }
            int i = 0;
            while (iter.hasNext()) {
                ItemStack item = iter.next();
                ItemMeta id = Utils.hasLore(item);
                if (id != null) {
                    if (BoundSoul.debug) {
                        BoundSoul.plugin.getLogger().info(b.getPlayer() + "|" + item);
                    }
                    List<String> lore = id.getLore();
                    int bindex = isBind(lore);
                    int index = lore.indexOf(autoBindLore);
                    if (index >= 0) {
                        lore.set(index, autoBindLore_later.replace("%player_name%", b.getPlayer().getName()));
                        id.setLore(lore);
                        item.setItemMeta(id);
                        b.changeInv(item, i);
                        
                        UtilsOfEffect.effect(player);
                        
                        if (id.hasDisplayName()) {
                            Utils.sendMessageToPlayer(player, YamlConfig.getConfig()
                                    .getString("BindSuccess")
                                    .replaceAll("&", "§")
                                    .replaceAll("%s%", id.getDisplayName()));
                        } else {
                            Utils.sendMessageToPlayer(player, YamlConfig.getConfig().getString("BindSuccess")
                                    .replaceAll("&", "§").replaceAll("%s%", String.valueOf(item.getTypeId())));
                        }
                        
                    }
                    if (bindex != -1) {
                        String a = getBinderName(lore.get(bindex));
                        if("".equals(a)){
                          return;
                        }
                        if(!getBinderName(lore.get(bindex)).equals(player.getName())){
                            if (BoundSoul.illegalPlayer.contains(player)) {
                                return;
                            }
                            if (id.hasDisplayName()) {
                                Utils.sendMessageToPlayer(player, "§f物品 " + id.getDisplayName() + " §f不属于你");
                            } else {
                                Utils.sendMessageToPlayer(player, "§f物品 " + item.getTypeId() + " §f不属于你");
                            }
                            Utils.sendMessageToPlayer(player, "§c不合法物品会被丢出");
                            lore.add(illegalItems);
                            id.setLore(lore);
                            item.setItemMeta(id);
                            if (!YamlConfig.getConfig().getBoolean("TackUpItem")) {
                                player.getWorld()
                                        .dropItem(player.getLocation(), item);
                            }
                            b.changeInv(null, i);
                        }
                        
                    }
                }
                i++;
            }
        }
    }
    
    
    public static int isBind(List<String> lore) {
        String args = autoBindLore_later.replace("%player_name%", "");
        if (lore == null || lore.isEmpty()) {
            return -1;
        }
        for (int i = lore.size() - 1; i >= 0; i--) {
            if (lore.get(i).toLowerCase().contains(args)) {
                return i;
            }
        }
        return -1;
    }
    
    
    
    
    public static String getBinderName(String args) {
        String key = autoBindLore_later.replace("%player_name%", "");
        return args.replace(key, "");
    }
}

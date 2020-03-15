/*
 * ©2021 August-soft Corporation. All rights reserved.
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

import net.augcloud.boundsoul.YamlConfig;
import net.augcloud.boundsoul.core.PlayerNDManager;
import net.augcloud.boundsoul.core.PlayerNDrop;
import net.augcloud.boundsoul.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/14 17:12
 * @description：
 * @version: $
 */
public class PlayerDeathListener implements Listener {
    
    
    @EventHandler(priority = EventPriority.LOWEST,ignoreCancelled = false)
    public void onEntityDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        if(e.getKeepInventory()){
          return;
        }
        
        
        PlayerNDrop playerNDrop = new PlayerNDrop();
        List<ItemStack> itemDrops = new ArrayList<>();
        //哎 懒得改了，原来Contents包括了ArmorContens
        List<ItemStack> itemStacks = Arrays.asList(player.getInventory().getExtraContents());
        
        for (int i = itemStacks.size() - 1; i >= 0; i--) {
            ItemStack item = itemStacks.get(i);
            if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta()) {
                itemStacks.set(i,null);
                itemDrops.add(item);
                continue;
            }
            ItemMeta ids = item.getItemMeta();
            if (!ids.hasLore()) {
                itemStacks.set(i,null);
                itemDrops.add(item);
                continue;
            }
            List<String> lore = ids.getLore();
            int index = ToolOfEvents.isBind(lore);
            if (index != -1 && ToolOfEvents.getBinderName(lore.get(index)).equals(player.getName())) {
            
            }else {
                itemStacks.set(i,null);
                itemDrops.add(item);
            }
        }
        playerNDrop.setInventories((ItemStack[]) itemStacks.toArray());
    
        
        List<ItemStack> itemStacks_ = Arrays.asList(player.getInventory().getArmorContents());
        for (int i = itemStacks_.size() - 1; i >= 0; i--) {
            ItemStack item = itemStacks_.get(i);
            if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta()) {
                itemStacks_.set(i,null);
                itemDrops.add(item);
                continue;
            }
            ItemMeta ids = item.getItemMeta();
            if (!ids.hasLore()) {
                itemStacks_.set(i,null);
                itemDrops.add(item);
                continue;
            }
            List<String> lore = ids.getLore();
            int index = ToolOfEvents.isBind(lore);
            if (index != -1 && ToolOfEvents.getBinderName(lore.get(index)).equals(player.getName())) {
            
            }else {
                itemDrops.add(item);
                itemStacks_.set(i,null);
            }
        }
        playerNDrop.setArmors((ItemStack[]) itemStacks_.toArray());
        PlayerNDManager.put(player,playerNDrop);
        e.setKeepInventory(true);
        Location location = player.getLocation();
        for (int i = itemDrops.size() - 1; i >= 0; i--) {
            ItemStack itemStack = itemDrops.get(i);
            if(itemStack==null || itemStack.getItemMeta().equals(Material.AIR)){
                continue;
            }
            location.getWorld().dropItem(location,itemDrops.get(i));
        }
        player.getInventory().setExtraContents(new ItemStack[player.getInventory().getExtraContents().length]);
        player.getInventory().setArmorContents(new ItemStack[player.getInventory().getArmorContents().length]);
        e.getDrops().clear();
        e.getDrops().addAll(itemDrops);
        System.out.println(itemDrops);
    }
}

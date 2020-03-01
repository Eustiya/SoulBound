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

import net.augcloud.boundsoul.Main;
import net.augcloud.boundsoul.YamlConfig;
import net.augcloud.boundsoul.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author £ºArisa
 * @date £ºCreated in 2020/3/1 19:14
 * @description£º
 * @version: $
 */
class PlayerInteractListener implements Listener {
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void whenPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        
        if (player.isOp()) {
            return;
        }
        ItemStack item = e.getItem();
        if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta()) {
            return;
        }
        ItemMeta ids = item.getItemMeta();
        if (!ids.hasLore()) {
            return;
        }
        List<String> lore = ids.getLore();
        int bindex = ToolOfEvents.isBind(lore);
        if (bindex != -1 && !ToolOfEvents.getBinderName(lore.get(bindex)).equals(player.getName())) {
            if (Main.illegalPlayer.contains(player)) {
                return;
            }
            
            lore.add(ToolOfEvents.illegalItems);
            ids.setLore(lore);
            item.setItemMeta(ids);
            e.setCancelled(true);
            Utils.sendMessageToPlayer(player, YamlConfig.getLang().getString("InteractCancelled"));
            
        }
    }
}

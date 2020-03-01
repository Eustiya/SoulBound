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

import net.augcloud.boundsoul.YamlConfig;
import net.augcloud.boundsoul.utils.Utils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * @author £ºArisa
 * @date £ºCreated in 2020/3/1 18:48
 * @description£º  1
 * @version: $
 */
class InventoryClickListener implements Listener {
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void whenInventoryClick(InventoryClickEvent e) {
        HumanEntity clicker = e.getWhoClicked();
        if (clicker instanceof Player) {
            Player player = (Player) clicker;
            if (player.isOp()) {
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (item == null || !item.hasItemMeta()) {
                return;
            }
            ItemMeta id = item.getItemMeta();
            List<String> lore;
            if (!id.hasLore()) {
                return;
            }
            lore = id.getLore();
            int index = ToolOfEvents.isBind(lore);
            if (index == -1) {
                return;
            }
            if (!ToolOfEvents.getBinderName(lore.get(index)).equals(player.getName())) {
                e.setCancelled(true);
                Utils.sendMessageToPlayer(player, YamlConfig.getLang().getString("ClickCancelled"));
                player.closeInventory();
            }
        }
    }
}

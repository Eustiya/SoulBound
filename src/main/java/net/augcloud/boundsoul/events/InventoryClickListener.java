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
import net.augcloud.boundsoul.core.PermitInventory;
import net.augcloud.boundsoul.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sun.awt.ConstrainableGraphics;

import java.util.List;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/1 18:48
 * @description：  1
 * @version: $
 */
class InventoryClickListener implements Listener {
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void whenInventoryClick(InventoryClickEvent e) {
        HumanEntity clicker = e.getWhoClicked();
        if (clicker instanceof Player) {
            Player player = (Player) clicker;
            if (player.isOp()) {
                System.out.println(1);
                return;
            }
            ItemStack item = e.getCurrentItem();
            if (item == null || !item.hasItemMeta()) {
                System.out.println(2);
                return;
            }
            ItemMeta id = item.getItemMeta();
            List<String> lore;
            if (!id.hasLore()) {
                System.out.println(3);
                return;
            }
            lore = id.getLore();
            int index = ToolOfEvents.isBind(lore);
            if (index == -1) {
                System.out.println(4);
                return;
            }
            boolean b  = true;
            ItemStack[] itemStacks = player.getInventory().getExtraContents();
            for (int i = itemStacks.length - 1; i >= 0; i--) {
                ItemStack itemStack = itemStacks[i];
                 if(itemStack == null || itemStack.getType().equals(Material.AIR)){
                     System.out.println(i);
                    b = false;
                    break;
                 }
            }
            System.out.println(b);
            if(b){
              e.setCancelled(true);
              Utils.sendMessageToPlayer(player, YamlConfig.getLang().getString("ClickCancelled2"));
              player.closeInventory();
                return;
            }
            String a =  ToolOfEvents.getBinderName(lore.get(index));
            if("".equals(a)){
               if(PermitInventory.isAllow(e.getClickedInventory())){
                  return;
               }
            }
            
            if (!a.equals(player.getName())) {
                e.setCancelled(true);
                Utils.sendMessageToPlayer(player, YamlConfig.getLang().getString("ClickCancelled"));
                player.closeInventory();
            }
        }
    }
}

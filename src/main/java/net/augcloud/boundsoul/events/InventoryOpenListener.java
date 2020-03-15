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

import net.augcloud.boundsoul.core.PlayerNDManager;
import net.augcloud.boundsoul.core.PlayerNDrop;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/7 8:28
 * @description：
 * @version: $
 */
public class InventoryOpenListener implements Listener {
    
    private static boolean open = false;
    private static String player;
    
    public static void setOpen(String _player){
         player = _player;
         open = true;
    }
    
    public static void close(){
        open = false;
    }
    
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void whenPlayerOpenInventory(InventoryOpenEvent e) {
        if(open){
            String a = e.getInventory().getType().toString();
           String b =  e.getPlayer().getName();
           if(b.equals(player)){
              e.getPlayer().sendMessage("Type is "+a);
           }
           
        }
    }
}

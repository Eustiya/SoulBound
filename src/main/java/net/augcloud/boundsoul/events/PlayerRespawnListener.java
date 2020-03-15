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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/14 17:14
 * @description：
 * @version: $
 */
public class PlayerRespawnListener implements Listener {
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if(!PlayerNDManager.contains(player)){
          return;
        }
        PlayerNDrop playerNDrop = PlayerNDManager.get(player);
        player.getInventory().setArmorContents(playerNDrop.getArmors());
        player.getInventory().setExtraContents(playerNDrop.getInventories());
        PlayerNDManager.remove(player);
    }


}

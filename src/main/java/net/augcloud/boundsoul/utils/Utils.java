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

package net.augcloud.boundsoul.utils;

import net.augcloud.boundsoul.YamlConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/1 18:14
 * @description：
 * @version: $
 */
public class Utils {
    
    
    
    public static boolean isNumber(String str) {
        return str.matches("^[0-9]*$");
    }
    
    public static String getLowerAndReplace(String args) {
        return args.toLowerCase().replaceAll("&", "§");
    }
    
    public static ItemMeta hasLore(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta id = item.getItemMeta();
            return id.hasLore() ? id : null;
        }
        return null;
    }
    
    public static void sendMessageToPlayer(Player player, String msg) {
        player.sendMessage(YamlConfig.getPluginPrefix() + msg.replaceAll("&","§"));
    }
}

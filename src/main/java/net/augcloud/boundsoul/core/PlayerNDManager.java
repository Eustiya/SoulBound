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

package net.augcloud.boundsoul.core;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/14 17:38
 * @description：
 * @version: $
 */
public class PlayerNDManager {
    
    private static HashMap<Player,PlayerNDrop> playerPlayerNDropHashMap = new HashMap<>();
    
    public static void put(Player player ,PlayerNDrop b){
        playerPlayerNDropHashMap.put(player,b);
    }
    
    public static PlayerNDrop get(Player player){
        return  playerPlayerNDropHashMap.get(player);
    }
    
    public static boolean contains(Player player){
        return playerPlayerNDropHashMap.containsKey(player);
    }
    
    public static void remove(Player player){
        playerPlayerNDropHashMap.remove(player);
    }
}

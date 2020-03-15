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
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/14 17:28
 * @description：
 * @version: $
 */
public class PlayerNDrop {
    private ItemStack[] inventories;
    private ItemStack[] armors;
    
    public PlayerNDrop(){
    
    }
    
    public PlayerNDrop(ItemStack[] inventories, ItemStack[] armors) {
        this.inventories = inventories;
        this.armors = armors;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlayerNDrop{");
        sb.append("inventories=").append(Arrays.toString(inventories));
        sb.append(", armors=").append(Arrays.toString(armors));
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerNDrop)) return false;
        PlayerNDrop that = (PlayerNDrop) o;
        return Arrays.equals(getInventories(), that.getInventories()) &&
                Arrays.equals(getArmors(), that.getArmors());
    }
    
    @Override
    public int hashCode() {
        int result = Arrays.hashCode(getInventories());
        result = 31 * result + Arrays.hashCode(getArmors());
        return result;
    }
    
    public ItemStack[] getArmors() {
        return armors;
    }
    
    public PlayerNDrop setArmors(ItemStack[] armors) {
        this.armors = armors;
        return this;
    }
    
    public ItemStack[] getInventories() {
        return inventories;
    }
    
    public PlayerNDrop setInventories(ItemStack[] inventories) {
        this.inventories = inventories;
        return this;
    }
}

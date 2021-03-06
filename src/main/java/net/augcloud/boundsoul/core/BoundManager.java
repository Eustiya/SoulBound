package net.augcloud.boundsoul.core;

import net.augcloud.boundsoul.PluginData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *  BoundManager class
 *
 * @author Arisa
 * @date 2016/10/31
 */
public class BoundManager {
    private final Player player;
    
    private final PlayerInventory inv;
    
    public BoundManager(Player player1, PlayerInventory inv1) {
        this.player = player1;
        this.inv = inv1;
        PluginData.bs.add(this);
    }
    
    public Player getPlayer() {
        return this.player;
    }
    
    public PlayerInventory getInv() {
        return this.inv;
    }
    
    public void changeInv(ItemStack item, int slot) {
        this.inv.setItem(slot, item);
        this.player.updateInventory();
    }
}

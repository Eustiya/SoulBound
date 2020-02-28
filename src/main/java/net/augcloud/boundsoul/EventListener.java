package net.augcloud.boundsoul;

import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;


/**
 * EventListen class
 *
 * @author Arisa
 * @date 2016/10/31
 */
class EventListener implements Listener {
    static String autoBindLore = "";
    
    static String autoBindLore_later = "";
    
    static String illegalItems = "";
    
    public static String getLowerRe(String args) {
        return args.toLowerCase().replaceAll("&", "§");
    }
    
    public static void updataInv(List<BoundManager> bs) {
        if (bs == null || bs.isEmpty()) {
            return;
        }
        for (BoundManager b : bs) {
            PlayerInventory inv = b.getInv();
            Iterator<ItemStack> iter = inv.iterator();
            final Player player = b.getPlayer();
            if (player.isOp()) {
                continue;
            }
            int i = 0;
            while (iter.hasNext()) {
                ItemStack item = iter.next();
                ItemMeta id = hasLore(item);
                if (id != null) {
                    if (Main.debug) {
                        Main.plugin.getLogger().info(b.getPlayer() + "|" + item);
                    }
                    List<String> Lore = id.getLore();
                    int bindex = isBind(Lore);
                    int index = Lore.indexOf(autoBindLore);
                    if (index >= 0) {
                        Lore.set(index, autoBindLore_later.replace("%player_name%", b.getPlayer().getName()));
                        id.setLore(Lore);
                        item.setItemMeta(id);
                        b.changeInv(item, i);
                        crateEffect(player.getLocation(), 50, 1, player.getWorld(), "FIREWORKS_SPARK");
                        (new BukkitRunnable() {
                            @Override
                            public void run() {
                                EventListener.fwtest(player);
                                cancel();
                            }
                        }).runTaskTimer(Main.plugin, 0L, 99999999999999L);
                        if (id.hasDisplayName()) {
                            sendMessageToPlayer(player, YamlConfig.getConfig()
                                    .getString("BindSuccess")
                                    .replaceAll("&", "§")
                                    .replaceAll("%s%", id.getDisplayName()));
                        } else {
                            sendMessageToPlayer(player, YamlConfig.getConfig().getString("BindSuccess")
                                    .replaceAll("&", "§").replaceAll("%s%", String.valueOf(item.getTypeId())));
                        }
                        if (!YamlConfig.getConfig().getBoolean("useSound")) {
                            if(Sound.valueOf("LEVEL_UP")!=null){
                                player.playSound(player.getLocation(),
                                        Sound.valueOf("LEVEL_UP"), 1.0F, 1.0F);
                            }else if(Sound.valueOf("ENTITY_PLAYER_LEVELUP")!=null ){
                                player.playSound(player.getLocation(),
                                        Sound.valueOf("ENTITY_PLAYER_LEVELUP"), 1.0F, 1.0F);
                            }
                            
                        }
                    }
                    if (bindex != -1 && !getBinderName(Lore.get(bindex)).equals(player.getName())) {
                        if (!Main.illegalPlayer.contains(player)) {
                            return;
                        }
                        if (id.hasDisplayName()) {
                            sendMessageToPlayer(player, "§f物品 " + id.getDisplayName() + " §f不属于你");
                        } else {
                            sendMessageToPlayer(player, "§f物品 " + item.getTypeId() + " §f不属于你");
                        }
                        sendMessageToPlayer(player, "§c不合法物品会被丢出");
                        Lore.add(illegalItems);
                        id.setLore(Lore);
                        item.setItemMeta(id);
                        if (!YamlConfig.getConfig().getBoolean("TackUpItem")) {
                            player.getWorld()
                                    .dropItem(player.getLocation(), item);
                        }
                        b.changeInv(null, i);
                    }
                }
                i++;
            }
        }
    }
    
    private static ItemMeta hasLore(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta id = item.getItemMeta();
            return id.hasLore() ? id : null;
        }
        return null;
    }
    
    public static int isBind(List<String> Lore) {
        String args = autoBindLore_later.replace("%player_name%", "");
        if (Lore == null || Lore.isEmpty()) {
            return -1;
        }
        for (int i = Lore.size() - 1; i >= 0; i--) {
            if (Lore.get(i).toLowerCase().contains(args)) {
                return i;
            }
        }
        return -1;
    }
    
    private static void crateEffect(final Location location, int amount, final int count, final World world, final String effect) {
        if (!YamlConfig.getConfig().getBoolean("useEffect")) {
            return;
        }
        location.setX(location.getX() + 0.5D);
        location.setY(location.getY() + 0.5D);
        location.setZ(location.getZ() + 0.5D);
        if (amount > 1) {
            for (int i = 0; amount > i; i++) {
                (new BukkitRunnable() {
                    int a = 0;
                    
                    @Override
                    public void run() {
                        if (this.a > count) {
                            cancel();
                            return;
                        }
                        this.a++;
                        world.playEffect(location, Effect.valueOf(effect), 1);
                    }
                }).runTaskTimer(Main.plugin, 0L, 10L);
            }
        } else {
            (new BukkitRunnable() {
                int a = 0;
                
                @Override
                public void run() {
                    if (this.a > count) {
                        cancel();
                        return;
                    }
                    this.a++;
                    world.playEffect(location, Effect.valueOf(effect), 1);
                }
            }).runTaskTimer(Main.plugin, 0L, 10L);
        }
    }
    
    private static void fwtest(Player player) {
        if (!YamlConfig.getConfig().getBoolean("useFireworks")) {
            return;
        }
        Firework fw = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fm = fw.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder().flicker(false).with(FireworkEffect.Type.BURST)
                .with(FireworkEffect.Type.BALL).with(FireworkEffect.Type.STAR).withColor(Color.YELLOW)
                .withColor(Color.ORANGE).withColor(Color.PURPLE).withColor(Color.GREEN).build());
        fm.setPower(1);
        fw.setFireworkMeta(fm);
    }
    
    private static void sendMessageToPlayer(Player player, String msg) {
        player.sendMessage(YamlConfig.getPlugin_Prefix() + msg);
    }
    
    private static String getBinderName(String args) {
        String key = autoBindLore_later.replace("%player_name%", "");
        return args.replace(key, "");
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void whenClick(InventoryClickEvent e) {
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
            int index = isBind(lore);
            if (index == -1) {
                return;
            }
            if (!getBinderName(lore.get(index)).equals(player.getName())) {
                e.setCancelled(true);
                sendMessageToPlayer(player, "操作不合法，试图取出没有处置权的物品");
                player.closeInventory();
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void whenPickup(PlayerPickupItemEvent e) {
        Player player = e.getPlayer();
        if (player.isOp()) {
            return;
        }
        if (e.getItem() == null) {
            return;
        }
        ItemStack item = e.getItem().getItemStack();
        if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta()) {
            return;
        }
        ItemMeta ids = item.getItemMeta();
        if (!ids.hasLore()) {
            return;
        }
        List<String> lore = ids.getLore();
        int index = isBind(lore);
        if (index != -1 && !getBinderName(lore.get(index)).equals(player.getName())) {
            e.setCancelled(true);
            sendMessageToPlayer(player, "拾起物品已经被别人绑定");
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void whenTakeup(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (player.isOp()) {
            return;
        }
        ItemStack item = e.getItemDrop().getItemStack();
        if (item == null || item.getType().equals(Material.AIR) || !item.hasItemMeta()) {
            return;
        }
        ItemMeta ids = item.getItemMeta();
        if (!ids.hasLore()) {
            return;
        }
        List<String> lore = ids.getLore();
        int index = isBind(lore);
        if (index != -1 && getBinderName(lore.get(index)).equals(player.getName())) {
            e.setCancelled(true);
            sendMessageToPlayer(player, "丢出自己已绑物品非法行为");
        }
    }
}

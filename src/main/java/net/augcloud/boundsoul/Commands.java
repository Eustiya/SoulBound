package net.augcloud.boundsoul;

import net.augcloud.boundsoul.core.PermitInventory;
import net.augcloud.boundsoul.events.InventoryOpenListener;
import net.augcloud.boundsoul.events.ToolOfEvents;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  MainCmd class
 *
 * @author Arisa
 * @date 2016/10/31
 */
class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command myCommand, String lable, String[] args) {
        if (args.length >= 1) {
            long endTime, startTime;
            String str;
            switch (str = args[0].toLowerCase()) {
                case "reload":
                    if ((!(sender instanceof Player) || !sender.isOp()) && !sender.hasPermission("BoundSoul.admin") &&
                            !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
                        sendMessage(sender, "执行此指令需 BoundSoul.admin 权限");
                        return true;
                    }
                    startTime = System.nanoTime();
                    try {
                        YamlConfig.regConfig();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ToolOfEvents.inits();
                    PermitInventory.inits();
                    endTime = System.nanoTime();
                    sendMessage(sender, "§a重载完成!耗时 " + ((endTime - startTime) / 1000000L) + "ms");
                    return true;
                case "unbind":
                    if (!(sender instanceof Player)) {
                        sendMessage(sender, "请以玩家身份输入此指令?");
                        return true;
                    }
                    if (sender.isOp() || sender.hasPermission("BoundSoul.admin")) {
                        Player player = (Player) sender;
                        ItemStack item = player.getItemInHand();
                        if (item != null && item.hasItemMeta()) {
                            ItemMeta id = item.getItemMeta();
                            if (id.hasLore()) {
                                List<String> lore = id.getLore();
                                int index = ToolOfEvents.isBind(lore);
                                if (index != -1) {
                                    lore.remove(index);
                                    id.setLore(lore);
                                    item.setItemMeta(id);
                                    player.updateInventory();
                                } else {
                                    sendMessage(sender, item.getItemMeta()
                                            .getDisplayName() + " §c并没有绑定!");
                                    return true;
                                }
                            }
                        }
                        sendMessage(sender, "§c操作不合法?");
                    } else {
                        sendMessage(sender, "执行此指令需 BoundSoul.admin 权限");
                    }
                    return true;
                case "bind":
                   
                    if (!(sender instanceof Player)) {
                        sendMessage(sender, "请以玩家身份输入此指令?");
                        return true;
                    }
                    if (sender.isOp() || sender.hasPermission("BoundSoul.used")) {
                        Player player = (Player) sender;
                        ItemStack item = player.getItemInHand();
                        if (item != null) {
                            ItemMeta id = item.getItemMeta();
                            List<String> lore = new ArrayList<>();
                            if (id != null && id.hasLore()) {
                                lore = id.getLore();
                                if (lore.contains(ToolOfEvents.autoBindLore)) {
                                    sendMessage(sender, "Lore已经有绑定标签");
                                    return true;
                                }
                                lore.add(ToolOfEvents.autoBindLore);
                            } else {
                                lore.add(ToolOfEvents.autoBindLore);
                            }
                            if (id != null && !lore.isEmpty()) {
                                id.setLore(lore);
                                item.setItemMeta(id);
                                player.updateInventory();
                                return true;
                            }
                        }
                        sendMessage(sender, "§c操作不合法?");
                    } else {
                        sendMessage(sender, "执行此指令需 BoundSoul.used 权限");
                    }
                    return true;
                case "checking":
                    if ((!(sender instanceof Player) || !sender.isOp()) && !sender.hasPermission("BoundSoul.admin") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
                        sendMessage(sender, "执行此指令需 BoundSoul.admin 权限");
                        return true;
                    }
                    if(args.length<=1){
                        sendMessage(sender, "§c参数太短");
                        return true;
                    }
                    if(!(sender instanceof Player)){
                        sendMessage(sender, "不要在后台执行这条指令......");
                       return true;
                    }
                    if("on".equals(args[1])){
                        InventoryOpenListener.setOpen(((Player)sender).getName());
                        sendMessage(sender, "§a查询已开启");
                    }else{
                        InventoryOpenListener.close();
                        sendMessage(sender, "§a查询已关闭");
                    }
                    return true;
                case "start":
                    
                    if ((!(sender instanceof Player) || !sender.isOp()) && !sender.hasPermission("BoundSoul.admin") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
                        sendMessage(sender, "执行此指令需 BoundSoul.admin 权限");
                        return true;
                    }
                    BoundSoul.threadManager.getBoundThread().startThread();
                    return true;
                case "setlore":
                    
                    if (sender instanceof Player && sender.isOp()) {
                      if (args.length >= 2) {
                          Player player = (Player) sender;
                          ItemStack item = player.getItemInHand();
                          if (item != null && item.getItemMeta() != null) {
                              ItemMeta id = item.getItemMeta();
                              List<String> list = new ArrayList<>();
                              if (id.hasLore()) {
                                list.addAll(id.getLore());
                              }
                              list.add(args[1].replaceAll("&", "§"));
                              list.add("&f评分: 202");
                              id.setLore(list);
                              id.setDisplayName("test");
                              item.setItemMeta(id);
                          } else {
                              sendMessage(sender, "手上拿了啥玩意？");
                          }
                      } else {
                          sendMessage(sender, "参数不正确?");
                      }
                    }
                    return true;
              default:
                return true;
              
            }
        }
        help(sender);
        return false;
    }
    
    private static void sendMessage(CommandSender sender, String msg) {
        boolean isPlayer = false;
        if (sender instanceof Player) {
            isPlayer = true;
        } else if (!(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            return;
        }
        if (isPlayer) {
            sender.sendMessage(YamlConfig.getPluginPrefix() + msg);
            return;
        }
        BoundSoul.plugin.getLogger().info(msg.replaceAll("§", ""));
    }
    
    private static void help(CommandSender sender) {
        boolean isPlayer = false;
        if (sender instanceof Player) {
            isPlayer = true;
        } else if (!(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            return;
        }
        if (isPlayer) {
            sender.sendMessage(" §fBoundSoul Used Help #Power by ?");
            sender.sendMessage("§7- §3/bs §a显示全部命令");
            sender.sendMessage("§7- §3/bs bind §a给手中物品添加绑定标签");
            sender.sendMessage("§7- §3/bs unbind §a解除手中物品绑定");
            sender.sendMessage("§7- §3/bs checking [on/off] §a开启或关闭容器debug模式，后台输出类型");
            sender.sendMessage("§7- §3/bs reload §a重新加载插件");
            return;
        }
        BoundSoul.plugin.getLogger().info(" BoundSoul Used Help #Power by Arisa");
        BoundSoul.plugin.getLogger().info("- /bs 显示全部命令");
        BoundSoul.plugin.getLogger().info("- /bs bind 给手中物品添加绑定标签");
        BoundSoul.plugin.getLogger().info("- /bs unbind 解除手中物品绑定");
        BoundSoul.plugin.getLogger().info("- /bs checking [on/off] 开启或关闭容器debug模式，后台输出类型");
        BoundSoul.plugin.getLogger().info("- /bs reload 重新加载插件");
    }
}

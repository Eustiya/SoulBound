package net.augcloud.boundsoul;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 *  MainCmd class
 *
 * @author Arisa
 * @date 2016/10/31
 */
class MainCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command myCommand, String lable, String[] args) {
        if (args.length >= 1) {
            long endTime, startTime;
            String str;
            switch ((str = args[0].toLowerCase()).hashCode()) {
                case -934641255:
                    if (!"reload".equals(str)) {
                      break;
                    }
                    if ((!(sender instanceof Player) || !sender.isOp()) && !sender.hasPermission("BoundSoul.admin") &&
                            !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
                        sendMessage(sender, "执行此指令需 BoundSoul.admin 权限");
                        return true;
                    }
                    startTime = System.nanoTime();
                    YamlConfig.regConfig();
                    EventListener.autoBindLore =
                            EventListener.getLowerRe(YamlConfig.getConfig().getString("AutoBindLore_Before"));
                    EventListener.autoBindLore_later =
                            EventListener.getLowerRe(YamlConfig.getConfig().getString("AutoBindLore_later"));
                    EventListener.illegalItems = EventListener.getLowerRe(YamlConfig.getConfig().getString("IllegalItems"));
                    endTime = System.nanoTime();
                    sendMessage(sender, "§a重载完成!耗时 " + ((endTime - startTime) / 1000000L) + "ms");
                    return true;
                case -840745386:
                    if (!"unbind".equals(str)) {
                      break;
                    }
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
                                List<String> Lore = id.getLore();
                                int index = EventListener.isBind(Lore);
                                if (index != -1) {
                                    Lore.remove(index);
                                    id.setLore(Lore);
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
                case 3023933:
                    if (!"bind".equals(str)) {
                      break;
                    }
                    if (!(sender instanceof Player)) {
                        sendMessage(sender, "请以玩家身份输入此指令?");
                        return true;
                    }
                    if (sender.isOp() || sender.hasPermission("BoundSoul.used")) {
                        Player player = (Player) sender;
                        ItemStack item = player.getItemInHand();
                        if (item != null) {
                            ItemMeta id = item.getItemMeta();
                            List<String> Lore = new ArrayList<>();
                            if (id != null && id.hasLore()) {
                                Lore = id.getLore();
                                if (Lore.contains(EventListener.autoBindLore)) {
                                    sendMessage(sender, "Lore已经有绑定标签");
                                    return true;
                                }
                                Lore.add(EventListener.autoBindLore);
                            } else {
                                Lore.add(EventListener.autoBindLore);
                            }
                            if (id != null && !Lore.isEmpty()) {
                                id.setLore(Lore);
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
                case 109757538:
                    if (!"start".equals(str)) {
                      break;
                    }
                    if ((!(sender instanceof Player) || !sender.isOp()) && !sender.hasPermission("BoundSoul.admin") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
                        sendMessage(sender, "执行此指令需 BoundSoul.admin 权限");
                        return true;
                    }
                    Main.BoundThread.startThread();
                    return true;
                case 1985708632:
                    if (!"setlore".equals(str)) {
                      break;
                    }
                    if (sender instanceof Player && sender.isOp()) {
                      if (args.length >= 2) {
                          Player player = (Player) sender;
                          ItemStack item = player.getItemInHand();
                          if (item != null && item.getItemMeta() != null) {
                              ItemMeta id = item.getItemMeta();
                              List<String> list = new ArrayList<String>();
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
            help(sender);
            return true;
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
            sender.sendMessage(YamlConfig.getPlugin_Prefix() + msg);
            return;
        }
        Main.plugin.getLogger().info(msg.replaceAll("§", ""));
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
            sender.sendMessage("§7- §3/bs reload §a重新加载插件");
            return;
        }
        Main.plugin.getLogger().info(" BoundSoul Used Help #Power by Arisa");
        Main.plugin.getLogger().info("- /bs 显示全部命令");
        Main.plugin.getLogger().info("- /bs bind 给手中物品添加绑定标签");
        Main.plugin.getLogger().info("- /bs unbind 解除手中物品绑定");
        Main.plugin.getLogger().info("- /bs reload 重新加载插件");
    }
}

package org.ponking.bpeg.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.ponking.bpeg.BukkitPluginExchangeGoods;

/**
 * 玩家服务器监听器
 *
 * @Author ponking
 * @Date 2021/4/9 17:56
 */
public class PlayerServerListener implements Listener {

    private final BukkitPluginExchangeGoods plugin;

    public PlayerServerListener(BukkitPluginExchangeGoods plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.addPlayer(player);
        plugin.getLogger().info("玩家 [" + player.getName() + "] 进入游戏!");
        PlayerInventory inventory = player.getInventory();
        ItemStack itemstack = new ItemStack(Material.TNT, 64);
        if (!inventory.contains(itemstack)) {
            // 将一组TNT放到玩家的背包里
            inventory.addItem(itemstack);
            //向玩家发送消息
            player.sendMessage("初始化背包成功");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getLogger().info("玩家 [" + event.getPlayer().getName() + "] 离开游戏!");
        plugin.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
//        if (plugin.isDebugging(event.getPlayer())) {
//            Location from = event.getFrom();
//            Location to = event.getTo();
//
//            plugin.getLogger().info(String.format("From %.2f,%.2f,%.2f to %.2f,%.2f,%.2f", from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ()));
//        }
    }
}

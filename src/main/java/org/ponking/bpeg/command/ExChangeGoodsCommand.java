package org.ponking.bpeg.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.ponking.bpeg.BukkitPluginExchangeGoods;
import org.ponking.bpeg.model.Order;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ponking
 * @Date 2021/4/9 18:12
 */
public class ExChangeGoodsCommand implements CommandExecutor {


    private final BukkitPluginExchangeGoods plugin;

    public ExChangeGoodsCommand(BukkitPluginExchangeGoods plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }


    /**
     * /exGoods 交易方块名称 交易方块的数量 换取方块名称 换取方块队数量 需要交易玩家名称
     *
     * @param sender
     * @param command
     * @param label
     * @param split
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (sender instanceof Player) {
            if (split.length == 5) {
                // 获取买方玩家
                Player curPlayer = (Player) sender;
                PlayerInventory curPlayerInventory = curPlayer.getInventory();
                Material buyerMaterial = Material.getMaterial(split[0]);
                if (buyerMaterial == null || !curPlayerInventory.contains(buyerMaterial)) {
                    sendMessage(curPlayer, "不存在" + buyerMaterial + "方块");
                    return false;
                }
                if (!curPlayerInventory.containsAtLeast(new ItemStack(buyerMaterial), Integer.parseInt(split[1]))) {
                    sendMessage(curPlayer, "超出" + buyerMaterial + "方块数量");
                    return false;
                }
                // 获取被操作玩家
                List<Player> otherPlayers = plugin.getPlayers().keySet().stream().
                        filter(item -> item.getName().equals(split[4])).collect(Collectors.toList());
                if (otherPlayers.size() != 1) {
                    sendMessage(curPlayer, "不存在该玩家！");
                    return false;
                }
                Player otherPlayer = otherPlayers.get(0);
                // 判断买方玩家需要转移的被操作玩家是否合法
                if(otherPlayer.equals(curPlayer)){
                    sendMessage(curPlayer, "自己与自己交易，你交易个寂寞！");
                    return false;
                }
                int buyerCount = Integer.parseInt(split[1]);
                Material sellerMaterial = Material.getMaterial(split[2]);
                int sellerCount = Integer.parseInt(split[3]);
                Order order = new Order(curPlayer, otherPlayer, buyerMaterial, sellerMaterial,
                        buyerCount, sellerCount, LocalDateTime.now(), null);
                this.plugin.addOrder(order);
                // end
                sendMessage(curPlayer, "申请成功，等待回复！");
                sendMessage(otherPlayer, "玩家"+curPlayer.getName()+"想用"+
                        buyerCount+"个"+buyerMaterial+"换取你的"+sellerCount+"个"+sellerMaterial);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

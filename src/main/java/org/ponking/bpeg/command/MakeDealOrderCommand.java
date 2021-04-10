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

import java.util.HashMap;

/**
 * @Author ponking
 * @Date 2021/4/11 0:26
 */
public class MakeDealOrderCommand implements CommandExecutor {


    private final BukkitPluginExchangeGoods plugin;

    public MakeDealOrderCommand(BukkitPluginExchangeGoods plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }


    /**
     * 当多个订单与用一个玩家相同时候可能会出错，日后再改，做简单先
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
            if (split.length == 1) {
                Player seller = (Player) sender;
                Order order = null;
                for (Order o : this.plugin.getOrders()) {
                    if (o.getSeller().equals(seller)) {
                        order = o;
                    }
                }
                if (order == null) {
                    return false;
                }
                if (!"Y".equals(split[0])) {
                    this.plugin.getOrders().remove(order);
                    return false;
                }
                // 判断当前玩家需要转移的方块数量是否合法
                Player buyer = order.getBuyer();
                Material sellerMaterial = order.getSellerMaterial();
                PlayerInventory curPlayerInventory = seller.getInventory();
                if (sellerMaterial == null || !curPlayerInventory.contains(sellerMaterial)) {
                    sendMessage(seller, "不存在" + sellerMaterial + "方块");
                    sendMessage(buyer, "对方不存在" + sellerMaterial + "方块");
                    return false;
                }
                if (!curPlayerInventory.containsAtLeast(new ItemStack(sellerMaterial), Integer.parseInt(split[1]))) {
                    sendMessage(seller, "超出" + sellerMaterial + "方块数量");
                    sendMessage(buyer, "对方没有那么多" + sellerMaterial + "方块数量");
                    return false;
                }
                exChangeMaterial(order);
                sendMessage(seller, "交易成功，请查收！");
                sendMessage(buyer, "交易成功，请查收！");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void exChangeMaterial(Order order) {
        Player buyer = order.getBuyer();
        Integer buyerCount = order.getBuyerCount();
        Material buyerMaterial = order.getBuyerMaterial();
        Player seller = order.getSeller();
        Integer sellerCount = order.getSellerCount();
        Material sellerMaterial = order.getSellerMaterial();
        PlayerInventory sellerInventory = seller.getInventory();
        // start 进行加减操作
        int count = sellerCount;
        // seller
        if (!sellerInventory.contains(sellerMaterial)) {
            sellerInventory.addItem(new ItemStack(sellerMaterial, count));
        } else {
            HashMap<Integer, ? extends ItemStack> all = sellerInventory.all(sellerMaterial);
            int curMaterialIndex = 0;
            // 默认第一个
            for (Integer index : all.keySet()) {
                curMaterialIndex = index;
                break;
            }
            all.get(curMaterialIndex).setAmount(all.get(curMaterialIndex).getAmount() + sellerCount);
        }
        // buyer
        PlayerInventory buyerInventory = buyer.getInventory();
        HashMap<Integer, ? extends ItemStack> buyerAll = buyerInventory.all(buyerMaterial);
        count = buyerCount;
        GiveGoodsCommand.subMaterial(count, buyerAll);
    }
}

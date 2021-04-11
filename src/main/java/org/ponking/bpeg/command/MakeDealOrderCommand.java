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
                Integer sellerCount = order.getSellerCount();
                PlayerInventory curPlayerInventory = seller.getInventory();
                if (sellerMaterial == null || !curPlayerInventory.contains(sellerMaterial)) {
                    sendMessage(seller, "不存在" + sellerMaterial + "方块");
                    sendMessage(buyer, "对方不存在" + sellerMaterial + "方块");
                    return false;
                }
                if (!curPlayerInventory.containsAtLeast(new ItemStack(sellerMaterial), sellerCount)) {
                    sendMessage(seller, "超出" + sellerMaterial + "方块数量");
                    sendMessage(buyer, "对方没有那么多" + sellerMaterial + "方块数量");
                    return false;
                }
                exChangeMaterial(order);
                this.plugin.getOrders().remove(order);
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

    /**
     * 交换
     *
     * @param order
     */
    public void exChangeMaterial(Order order) {
        Player buyer = order.getBuyer();
        Integer buyerCount = order.getBuyerCount();
        Material buyerMaterial = order.getBuyerMaterial();

        Player seller = order.getSeller();
        Integer sellerCount = order.getSellerCount();
        Material sellerMaterial = order.getSellerMaterial();

        doExChangeMaterial(buyer, sellerMaterial, sellerCount,
                seller, sellerMaterial, sellerCount);

        doExChangeMaterial(seller, buyerMaterial, buyerCount,
                buyer, buyerMaterial, buyerCount);
    }

    /**
     * 交换核心方法
     *
     * @param p1
     * @param m1
     * @param c1
     * @param p2
     * @param m2
     * @param c2
     */
    public void doExChangeMaterial(Player p1, Material m1, int c1,
                                   Player p2, Material m2, int c2) {

        // add
        PlayerInventory sellerInventory = p1.getInventory();
        if (!sellerInventory.contains(m1)) {
            sellerInventory.addItem(new ItemStack(m1, c1));
        } else {
            HashMap<Integer, ? extends ItemStack> all = sellerInventory.all(m1);
            for (ItemStack itemStack : all.values()) {
                itemStack.setAmount(itemStack.getAmount() + c1);
                break;
            }
        }

        // sub
        PlayerInventory buyerInventory = p2.getInventory();
        HashMap<Integer, ? extends ItemStack> buyerAll = buyerInventory.all(m2);
        for (ItemStack value : buyerAll.values()) {
            if (c2 == 0) {
                break;
            }
            int preAmount = value.getAmount();
            if (preAmount - c2 >= 0) {
                c2 = 0;
                value.setAmount(preAmount - c2);
            } else {
                c2 = c2 - preAmount;
                value.setAmount(0);
            }
        }
    }
}

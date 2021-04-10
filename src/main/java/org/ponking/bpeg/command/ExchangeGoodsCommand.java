package org.ponking.bpeg.command;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.ponking.bpeg.BukkitPluginExchangeGoods;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ponking
 * @Date 2021/4/9 18:12
 */
public class ExchangeGoodsCommand implements CommandExecutor {


    private final BukkitPluginExchangeGoods plugin;

    public ExchangeGoodsCommand(BukkitPluginExchangeGoods plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(message);
    }


    /**
     * /exGoods  [方块名称] [数量] [玩家名字]
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
            if (split.length == 3) {
                // 获取当前操作玩家
                Player curPlayer = (Player) sender;
                // 判断当前玩家需要转移的方块数量是否合法
                PlayerInventory curPlayerInventory = curPlayer.getInventory();
                Material material = Material.getMaterial(split[0]);
                if (material == null || !curPlayerInventory.contains(material)) {
                    sendMessage(curPlayer, "不存在" + material + "方块");
                    return false;
                }
                if (!curPlayerInventory.containsAtLeast(new ItemStack(material), Integer.parseInt(split[1]))) {
                    sendMessage(curPlayer, "超出" + material + "方块数量");
                    return false;
                }
                // 获取被操作玩家
                List<Player> otherPlayers = plugin.getPlayers().keySet().stream().
                        filter(item -> item.getName().equals(split[2])).collect(Collectors.toList());
                if (otherPlayers.size() != 1) {
                    sendMessage(curPlayer, "不存在该玩家！");
                    return false;
                }
                Player otherPlayer = otherPlayers.get(0);
                // 判断当前玩家需要转移的被操作玩家是否合法
                if(otherPlayer.equals(curPlayer)){
                    sendMessage(curPlayer, "自己给自己，你给个寂寞！");
                    return false;
                }
                PlayerInventory otherInventory = otherPlayer.getInventory();
                // start 进行加减操作
                int count = Integer.parseInt(split[1]);
                //被操作玩家
                if (!otherInventory.contains(material)) {
                    otherInventory.addItem(new ItemStack(material, count));
                } else {
                    HashMap<Integer, ? extends ItemStack> all = otherInventory.all(material);
                    int curMaterialIndex = 0;
                    // 默认第一个
                    for (Integer index : all.keySet()) {
                        curMaterialIndex = index;
                        break;
                    }
                    all.get(curMaterialIndex).setAmount(all.get(curMaterialIndex).getAmount() + Integer.parseInt(split[1]));
                }
                //操作玩家
                HashMap<Integer, ? extends ItemStack> curAll = curPlayerInventory.all(material);
                for (ItemStack value : curAll.values()) {
                    if (count == 0) {
                        break;
                    }
                    int preAmount = value.getAmount();
                    value.setAmount(Math.max(preAmount - count, 0));
                    count = preAmount - count >= 0 ? 0 : count - preAmount;
                }
                // end
                sendMessage(otherPlayer, "你收到" + curPlayer.getName() + "的" + Integer.parseInt(split[1]) + "个" + material);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

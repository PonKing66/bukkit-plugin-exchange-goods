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
                assert material != null;
                if (!curPlayerInventory.contains(material)) {
                    curPlayer.sendMessage("不存在" + material + "方块");
                    return false;
                }
                if (!curPlayerInventory.containsAtLeast(new ItemStack(material), Integer.parseInt(split[1]))) {
                    curPlayer.sendMessage("超出" + material + "方块数量");
                    return false;
                }
                // 获取被操作玩家
                List<Player> otherPlayers = plugin.getPlayers().keySet().stream().
                        filter(item -> item.getName().equals(split[2])).collect(Collectors.toList());
                if (otherPlayers.size() != 1) {
                    curPlayer.sendMessage("不存在该玩家！");
                }
                Player otherPlayer = otherPlayers.get(0);
                PlayerInventory otherInventory = otherPlayer.getInventory();
                // 进行加减操作
                if (!otherInventory.contains(material)) {
                    int count = Integer.parseInt(split[1]);
                    //被操作玩家
                    otherInventory.addItem(new ItemStack(material, count));

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
                } else {
                    HashMap<Integer, ? extends ItemStack> all = otherInventory.all(material);
                    int curMaterialIndex = 0;
                    int count = Integer.parseInt(split[1]);
                    // 默认第一个
                    for (Integer index : all.keySet()) {
                        curMaterialIndex = index;
                        break;
                    }
                    all.get(curMaterialIndex).setAmount(all.get(curMaterialIndex).getAmount() + Integer.parseInt(split[1]));
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
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}

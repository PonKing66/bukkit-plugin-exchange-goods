package org.ponking.bpeg.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ponking.bpeg.BukkitPluginExchangeGoods;

/**
 * 恢复存储点
 *
 * @Author ponking
 * @Date 2021/4/9 20:06
 */
public class ReplyLocationCommand implements CommandExecutor {

    private final BukkitPluginExchangeGoods plugin;


    public ReplyLocationCommand(BukkitPluginExchangeGoods plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!this.plugin.getLocationHashMap().containsKey(player)) {
                player.sendMessage("没有存储点，请先存储！");
                return false;
            }
            Location location = this.plugin.getLocationHashMap().get(player);
            try {
                double x = Double.parseDouble(String.valueOf(location.getX()));
                double y = Double.parseDouble(String.valueOf(location.getY()));
                double z = Double.parseDouble(String.valueOf(location.getZ()));

                player.teleport(new Location(player.getWorld(), x, y, z));
            } catch (NumberFormatException ex) {
                player.sendMessage("恢复存储点失败");
            }
            plugin.getLogger().info("恢复存储点");
            return true;
        } else {
            return false;
        }
    }
}

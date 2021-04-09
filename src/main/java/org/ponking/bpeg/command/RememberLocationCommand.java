package org.ponking.bpeg.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ponking.bpeg.BukkitPluginExchangeGoods;

import java.util.HashMap;

/**
 * 记录点
 *
 * @Author ponking
 * @Date 2021/4/9 19:56
 */
public class RememberLocationCommand implements CommandExecutor {

    private final BukkitPluginExchangeGoods plugin;


    public RememberLocationCommand(BukkitPluginExchangeGoods plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getLocation();
            this.plugin.getLocationHashMap().put(player, location);
            player.sendMessage("记录存储点");
            return true;
        } else {
            return false;
        }
    }
}

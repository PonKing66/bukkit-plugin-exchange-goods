package org.ponking.bpeg.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.ponking.bpeg.BukkitPluginExchangeGoods;

/** 开启debug模式
 * @author ponking
 */
public class DebugCommand implements CommandExecutor {
    private final BukkitPluginExchangeGoods plugin;

    public DebugCommand(BukkitPluginExchangeGoods plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] split) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            plugin.setDebugging(player, !plugin.isDebugging(player));
            plugin.getLogger().info("当前处于debug模式："+plugin.isDebugging(player));
            return true;
        } else {
            return false;
        }
    }
}

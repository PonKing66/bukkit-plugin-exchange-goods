package org.ponking.bpeg;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.ponking.bpeg.command.DebugCommand;
import org.ponking.bpeg.command.ExchangeGoodsCommand;
import org.ponking.bpeg.command.RememberLocationCommand;
import org.ponking.bpeg.command.ReplyLocationCommand;
import org.ponking.bpeg.listener.PlayerServerListener;

import java.util.HashMap;

/**
 * @author ponking
 */
public final class BukkitPluginExchangeGoods extends JavaPlugin {

    private final HashMap<Player, Boolean> players = new HashMap<Player, Boolean>();

    private final HashMap<Player, Location> locationHashMap = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("加载BukkitPluginExchangeGoods...");
        // Register our events
        registerEvent(getServer().getPluginManager());
        // Register our commands
        registerCommand();


        PluginDescriptionFile pdfFile = this.getDescription();
        getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " 启动成功！");

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("关闭服务器成功！");
    }

    public void registerEvent(PluginManager pm) {
        final PlayerServerListener playerListener = new PlayerServerListener(this);
        pm.registerEvents(playerListener, this);
    }

    public void registerCommand() {
        getCommand("exGoods").setExecutor(new ExchangeGoodsCommand(this));
        getCommand("debug").setExecutor(new DebugCommand(this));
        getCommand("rml").setExecutor(new RememberLocationCommand(this));
        getCommand("rel").setExecutor(new ReplyLocationCommand(this));
    }


    public boolean isDebugging(final Player player) {
        if (players.containsKey(player)) {
            return players.get(player);
        } else {
            return false;
        }
    }

    public void setDebugging(final Player player, final boolean value) {
        players.put(player, value);
    }

    public void addPlayer(Player player) {
        players.put(player, false);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }


    public HashMap<Player, Boolean> getPlayers() {
        return players;
    }

    public HashMap<Player, Location> getLocationHashMap() {
        return locationHashMap;
    }
}

package me.driftay.ctf;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import me.driftay.ctf.commands.CmdCTF;
import me.driftay.ctf.file.CustomFile;
import me.driftay.ctf.file.impl.MessageFile;
import me.driftay.ctf.listeners.BlockBreakListener;
import me.driftay.ctf.manager.Winner;
import me.driftay.ctf.util.DataFile;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public class SaberCTF extends JavaPlugin implements Listener {
    public static SaberCTF instance;
    public DataFile winnerFile;

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getDataFolder().mkdirs();
        Collections.singletonList(new MessageFile()).forEach(CustomFile::init);
        getWorldGuard();
        winnerFile = new DataFile(this, "winners");
        getCommand("ctf").setExecutor(new CmdCTF());
        getCommand("ctf").setTabCompleter(new CmdCTF());
        this.getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        Winner.winnerNames.addAll(winnerFile.getStringList("winners"));
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin w = this.getServer().getPluginManager().getPlugin("WorldGuard");
        if (w == null || !(w instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) w;
    }
}

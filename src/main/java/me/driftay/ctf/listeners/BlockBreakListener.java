package me.driftay.ctf.listeners;

import com.massivecraft.factions.util.XMaterial;
import me.driftay.ctf.commands.CmdCTF;
import me.driftay.ctf.manager.BlockManager;
import me.driftay.ctf.manager.CTFRegion;
import me.driftay.ctf.manager.Winner;
import me.driftay.ctf.util.Message;
import me.driftay.ctf.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.HashMap;

import static me.driftay.ctf.util.Utils.config;
import static me.driftay.ctf.util.Utils.ctfBlockDurability;

public class BlockBreakListener implements Listener {

    private HashMap<String, Integer> timesMined = new HashMap<>();
    private CTFRegion regions = new CTFRegion();
    private Winner winner = new Winner();



    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Material item = XMaterial.matchXMaterial(Utils.config.getString("CTF.Block-Type")).parseMaterial();

        Player p = e.getPlayer();
        if (!regions.isInCTFRegion(p)) return;
        if (e.getBlock().getType() != item) return;
        if (!BlockManager.getBlockLocation(e.getBlock())) return;

        if (!CmdCTF.isRunning) {
            p.sendMessage(Message.CTF_NOT_ACTIVE.getMessage());
            e.setCancelled(true);
            return;
        }

        if (!timesMined.containsKey(p.getName())) {
            timesMined.put(p.getName(), 1);
            p.sendMessage(Message.CTF_MINE_NOTIFICATION.getMessage().replace("{count}", String.valueOf(ctfBlockDurability - timesMined.get(p.getName()))));
            e.setCancelled(true);
            if(config.getBoolean("CTF.Use-Particle-System")) {
                Utils.spawnAnimation(p, e.getBlock());
            }
            return;
        }
        timesMined.put(p.getName(), timesMined.get(p.getName()) + 1);
        if (timesMined.get(p.getName()) == ctfBlockDurability) {
            Bukkit.broadcastMessage(Message.CTF_WINNER_BROADCAST.getMessage().replace("{player}", p.getName()));
            Winner.winnerNames.add(p.getName());
            e.setCancelled(true);
            CmdCTF.isRunning = false;
            timesMined.clear();
            winner.setWinnersToFile();
            return;
        }
        p.sendMessage(Message.CTF_MINE_NOTIFICATION.getMessage().replace("{count}", String.valueOf(ctfBlockDurability - timesMined.get(p.getName()))));
        if(config.getBoolean("CTF.Use-Particle-System")) {
            Utils.spawnAnimation(p, e.getBlock());
        }
        e.setCancelled(true);
    }
}
package me.driftay.ctf.manager;

import me.driftay.ctf.SaberCTF;
import me.driftay.ctf.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Winner {
    public static List<String> winnerNames = new ArrayList<>();

    public void setWinnersToFile(){
        SaberCTF.instance.winnerFile.getConfiguration().set("winners", winnerNames);
        SaberCTF.instance.winnerFile.save();
    }

    public static void runCommands(Player p) {
        for (String cmd : Utils.config.getStringList("CTF.Winning-Commands")) {
            cmd = cmd.replace("{player}", p.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
        }
    }
}

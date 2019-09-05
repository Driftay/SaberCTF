package me.driftay.ctf.util;

import me.driftay.ctf.SaberCTF;
import org.bukkit.ChatColor;

public class Logger {

    public static void print(String message, PrefixType type) {
        SaberCTF.instance.getServer().getConsoleSender().sendMessage(type.getPrefix() + message);
    }

    public enum PrefixType {

        WARNING(ChatColor.RED + "WARNING: "), NONE(""), DEFAULT(ChatColor.GOLD + "[SaberCore] "), FAILED(ChatColor.RED + "FAILED: ");

        private String prefix;

        PrefixType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }

    }

}

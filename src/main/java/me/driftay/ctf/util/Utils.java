package me.driftay.ctf.util;

import me.driftay.ctf.threads.SkullProfile;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static me.driftay.ctf.SaberCTF.instance;

public class Utils {


    public static FileConfiguration config = instance.getConfig();

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> colorList(List<String> string) {
        List<String> colored = new ArrayList<>();
        for (String line : string) {
            colored.add(color(line));
        }
        return colored;
    }

    public static void sendConsole(String command) {
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
    }

    public static int ctfBlockDurability = config.getInt("CTF.Block-Durability");

    public static Location faceEntity(Location location, Entity entity) {
        Vector direction = location.toVector().subtract(entity.getLocation().toVector());
        direction.multiply(-1);
        location.setDirection(direction);
        return location;
    }

    public static Location center(Location location) {
        String x = "" + location.getX();
        String z = "" + location.getZ();
        if(x.contains(".")) x = x.substring(0, x.indexOf("."));
        if(z.contains(".")) z = z.substring(0, z.indexOf("."));
        x+=".5";
        z+=".5";
        location.setX(Double.parseDouble(x));
        location.setZ(Double.parseDouble(z));
        return resetRotation(location);
    }

    public static void spawnAnimation(Player p, Block block){
        ArmorStand armorStand = block.getLocation().getWorld().spawn(
                Utils.faceEntity((block.getLocation().add(0.5, 0.5, 0.5)), p), ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setBasePlate(false);

        armorStand.setHelmet(constructArmorSkull());
        new BukkitRunnable() {
            int frames = 0;

            public void run() {
                if (frames > 30) {
                    this.cancel();
                    armorStand.remove();
                    HandlerList.unregisterAll((Listener) instance);
                    return;
                }
                if (frames > 10) {
                    Location newLocation = armorStand.getLocation().add(0, 0.2, 0);
                    newLocation.setYaw(newLocation.getYaw() + 18f);
                    armorStand.teleport(newLocation);
                }
                p.spigot().playEffect(armorStand.getLocation().clone().add(0, 0.5, 0), Effect.getByName(Utils.config.getString("CTF.Particle-Name")),
                        0, 0, 0, 0, 0,
                        0, 3, 15);
                frames++;
            }
        }.runTaskTimer(instance, 0, 1L);
    }

    public static ItemStack constructArmorSkull() {
        ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

        skullMeta.setDisplayName(null);
        itemStack.setItemMeta(skullMeta);

        new SkullProfile(Utils.config.getString("CTF.Animation-Skull-Hash"))
                .applyTextures(itemStack);

        return itemStack;
    }

    public static Location resetRotation(Location location) {
        location.setYaw(0f);
        location.setPitch(0f);
        return location;
    }
}

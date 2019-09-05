package me.driftay.ctf.manager;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import me.driftay.ctf.SaberCTF;
import me.driftay.ctf.util.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CTFRegion {

    public boolean isInCTFRegion(Player p) {
        Location loc = p.getLocation();
        WorldGuardPlugin worldGuard = SaberCTF.instance.getWorldGuard();
        RegionManager regionManager = worldGuard.getRegionManager(loc.getWorld());
        ApplicableRegionSet rg = regionManager.getApplicableRegions(loc);
        String ctfRegionName = Utils.config.getString("CTF.Region-Name");
        if (rg.size() == 0) {
            return false;
        }
        for (ProtectedRegion region : rg) {
            if (region.getId().equalsIgnoreCase(ctfRegionName)) {
                return true;
            }
        }
        return false;
    }
}

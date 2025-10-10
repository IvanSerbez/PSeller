package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.Messages.psMessages;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PrivateOperations {


    public static boolean privateop(Player p) {

        BlockVector3 pos1, pos2;
        World bukkitWorld = p.getWorld();
        BukkitPlayer player = BukkitAdapter.adapt(p);
        com.sk89q.worldedit.world.World CurrentWorld = BukkitAdapter.adapt(bukkitWorld);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(player);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager manager = container.get(CurrentWorld);


        if (manager == null) {
            return false;
        }


        try {

            Region region = session.getSelection(player.getWorld());

            pos1 = region.getMinimumPoint();
            pos2 = region.getMaximumPoint();

            // проверка на пересечение региона с другими уже существующими регионами
            try {
                ProtectedRegion test = new ProtectedCuboidRegion("dummy", pos1, pos2);
                ApplicableRegionSet set = manager.getApplicableRegions(test);
                if (!set.getRegions().isEmpty()) {
                    psMessages.PrivateAreaErrorMess(p);
                    return false;
                }


            } catch (Exception e) {
                p.sendMessage("Exception " + e);
                return false;
            }


            return true;

        } catch (IncompleteRegionException e) {
           return false;
        }


    }

}

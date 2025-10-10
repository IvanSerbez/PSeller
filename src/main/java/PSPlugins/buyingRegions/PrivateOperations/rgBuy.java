package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.Commands.PsCommand;
import PSPlugins.buyingRegions.Messages.psMessages;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Set;


public class rgBuy {








    public static void buyRegion(Player p, String privateName) {

        BlockVector3 pos1, pos2;
        World bukkitWorld = p.getWorld();
        BukkitPlayer player = BukkitAdapter.adapt(p);
        com.sk89q.worldedit.world.World CurrentWorld = BukkitAdapter.adapt(bukkitWorld);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(player);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager manager = container.get(CurrentWorld);





            if (manager == null) {
                return;
            }


        try {

                Region region = session.getSelection(player.getWorld());

                pos1 = region.getMinimumPoint();
                pos2 = region.getMaximumPoint();

                try {
                    ProtectedRegion test = new ProtectedCuboidRegion("dummy", pos1, pos2);
                    ApplicableRegionSet set = manager.getApplicableRegions(test);
                    if(!set.getRegions().isEmpty())
                    {
                        psMessages.PrivatAreaErrorMess(p);return;
                    }
                } catch (Exception e){
                    p.sendMessage("Exception " + e);
                }



                if (manager.getRegion(privateName) != null) {
                    psMessages.PrivateNameErrorMess(p, privateName);
                    return;
                }
                ProtectedRegion privat = new ProtectedCuboidRegion(privateName, pos1, pos2);
                privat.getOwners().addPlayer(p.getUniqueId());
                manager.addRegion(privat);


            } catch (Exception e) {
                psMessages.NotFoundSelectionMess(p);

            }
    }

    public static void buyRegion(Player p){

        psMessages.NotFoundNamePrivateMess(p);

    }

}





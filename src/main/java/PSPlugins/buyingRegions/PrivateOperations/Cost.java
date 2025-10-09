package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.Messages.psMessages;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class Cost {

    public static void costRegion(Player p)
    {
        BukkitPlayer Player = BukkitAdapter.adapt(p);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(Player);

        try {
            Region region = session.getSelection(Player.getWorld());
            BlockVector3 pos1 = region.getMinimumPoint();
            BlockVector3 pos2 = region.getMaximumPoint();


            int sizeX = Math.abs(pos2.getX() - pos1.getX()) + 1;
            int sizeY = Math.abs(pos2.getY() - pos1.getY()) + 1;
            int sizeZ = Math.abs(pos2.getZ() - pos1.getZ()) + 1;

            int summSize = sizeX * sizeY * sizeZ;

            int price = (int) (200 + (-0.0000004 * Math.pow(sizeX,2) * Math.pow(sizeZ,2) + 0.40 * sizeX * sizeZ) * (1 + sizeY / 512));



            psMessages.CostMess(p,price, new Vector(sizeX,sizeY,sizeZ), summSize);



        } catch (Exception e) {
            p.sendMessage("Выделение не найдено или неполное.");
        }
    }






    }


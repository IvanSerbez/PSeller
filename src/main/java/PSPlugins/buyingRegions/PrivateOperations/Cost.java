package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.Messages.psMessages;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


public class Cost {



    private static CostDataBox mathOperation(Player p)
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

             return new CostDataBox(p,price,new Vector(sizeX,sizeY,sizeZ),summSize);




        } catch (Exception e) {
            psMessages.NotFoundSelectionMess(p);
            return null;
        }

    }

    public static void costRegion(Player p)
    {

        CostDataBox dataBox = mathOperation(p);
        if(dataBox !=null)
        psMessages.CostMess(p,dataBox.price, dataBox.size,dataBox.summSize);

    }

    public static void sizeRegion(Player p)
    {
        CostDataBox dataBox = mathOperation(p);
        if(dataBox !=null)
        psMessages.SizeMess(p,dataBox.size,dataBox.summSize);

    }




    }




    class CostDataBox
    {
       public Player player;
       public int price, summSize;
       public Vector size;
        CostDataBox(Player p, int price, Vector size, int summSize)
        {
            this.player = p;
            this.price = price;
            this.summSize = summSize;
            this.size = size;

        }
    }


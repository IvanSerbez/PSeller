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


    ///  Создает и возвращает данные выделения. подсчет стоимости. размеры и тд.
    private static CostDataBox mathOperation(Player p)
    {
        BukkitPlayer Player = BukkitAdapter.adapt(p);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(Player);

        try {

            ///  взятие простого выделения
            Region region = session.getSelection(Player.getWorld());
            BlockVector3 pos1 = region.getMinimumPoint();
            BlockVector3 pos2 = region.getMaximumPoint();

            ///  получаем размеры XYZ
            int sizeX = Math.abs(pos2.getX() - pos1.getX()) + 1;
            int sizeY = Math.abs(pos2.getY() - pos1.getY()) + 1;
            int sizeZ = Math.abs(pos2.getZ() - pos1.getZ()) + 1;

            ///  получаем суммарное кол-во. блоков выделения
            int summSize = sizeX * sizeY * sizeZ;

            ///  формулы стоимости региона и суб региона
                         //C = 200 + (-0.0000004 * X^2 * Z^2 + 0.40 * X * Z) * (1 + Y / 512)
            int price = (int)(200 + (-0.0000004 * Math.pow(sizeX,2) * Math.pow(sizeZ,2) + 0.40 * sizeX * sizeZ) * (1 + (double) sizeY / 512));
            int pricesub = (int)(200 + (-0.0000004 * Math.pow(sizeX,2) * Math.pow(sizeZ,2) + 0.40 * sizeX * sizeZ) * (1 + (double) sizeY / 512)*25/100);

            /// возвращает Класс данных выделения
             return new CostDataBox(p,price,new Vector(sizeX,sizeY,sizeZ),summSize,pricesub);


        } catch (Exception e) {return null;}

    }

    ///  команда ps cost посчета стоимости и размеров региона
    public static void costRegion(Player p)
    {
        CostDataBox dataBox = getCostDataBox(p);
        if(dataBox !=null) {
            psMessages.CostMess(p, dataBox.summSize);
        } else { psMessages.NotFoundSelectionMess(p);}
    }

    ///  получение данных выделения (можно заменить на прямое использование mathOperation)
    public static CostDataBox getCostDataBox(Player p)
    { CostDataBox dataBox = mathOperation(p);
        if(dataBox != null){return dataBox;} else return null;
    }

    ///  команда ps size подсчета размеров выделения
    public static void sizeRegion(Player p)
    {
        CostDataBox dataBox = getCostDataBox(p);
        if(dataBox !=null) {
        psMessages.SizeMess(p,dataBox.summSize);}else { psMessages.NotFoundSelectionMess(p);}

    }




    }





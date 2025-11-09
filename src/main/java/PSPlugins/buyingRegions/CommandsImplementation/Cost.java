package PSPlugins.buyingRegions.CommandsImplementation;

import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Files.GetOptionsConfig;
import PSPlugins.buyingRegions.Messages.psMessages;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class Cost {


    ///  Создает и возвращает данные выделения. подсчет стоимости. размеры и тд.
    private static CostDataBox mathOperation(Player p)
    {
        BukkitPlayer Player = BukkitAdapter.adapt(p);
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(Player);

        try {

            GetOptionsConfig optionsConfig = new GetOptionsConfig();
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



            ///  Получаем кол-во платных приватов у игрока
            int numberOfPrivates = 0;
            try {numberOfPrivates = PrivateOperations.getPaidPrivates(p).size();}catch (Exception e){}

            ///  формулы стоимости региона и суб региона

            int price = (int) (150 + (numberOfPrivates * optionsConfig.region_multiplier) + (10 / (1 + Math.exp(numberOfPrivates * optionsConfig.region_ratio))) * Math.sqrt(numberOfPrivates * optionsConfig.volume_ratio * summSize));
            /// ВАЖНО!!!! формула на суб приваты не официальная! Требуется замена на согласованную формулу.!!!
            int pricesub = (int) (50 + (10 / (1 + Math.exp(numberOfPrivates * optionsConfig.region_ratio))) * Math.sqrt(numberOfPrivates * optionsConfig.volume_ratio * summSize));


            /* Старые формулы
                         //C = 200 + (-0.0000004 * X^2 * Z^2 + 0.40 * X * Z) * (1 + Y / 512)
            int price = (int)(200 + (-0.0000004 * Math.pow(sizeX,2) * Math.pow(sizeZ,2) + 0.40 * sizeX * sizeZ) * (1 + (double) sizeY / 512));
            int pricesub = (int)(200 + (-0.0000004 * Math.pow(sizeX,2) * Math.pow(sizeZ,2) + 0.40 * sizeX * sizeZ) * (1 + (double) sizeY / 512)*25/100);
            */
            /// возвращает Класс данных выделения,m
             return new CostDataBox(p,price,new Vector(sizeX,sizeY,sizeZ),summSize,pricesub,numberOfPrivates);


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





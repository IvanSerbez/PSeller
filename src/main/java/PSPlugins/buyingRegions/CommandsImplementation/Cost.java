package PSPlugins.buyingRegions.CommandsImplementation;

import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Files.GetOptionsConfig;
import PSPlugins.buyingRegions.Files.OptionsConfig;
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
            int numberOfPrivates = 1;
            try {numberOfPrivates = PrivateOperations.getPaidPrivates(p).size();}catch (Exception e){}
            if (numberOfPrivates == 0){numberOfPrivates = 1;}

            ///  формулы стоимости региона и суб региона

            int price = (int) (optionsConfig.start_cost + (numberOfPrivates * optionsConfig.region_multiplier) + (10 / (1 + Math.exp(numberOfPrivates * optionsConfig.region_ratio))) * Math.sqrt(numberOfPrivates * optionsConfig.volume_ratio * summSize));
            /// ВАЖНО!!!! формула на суб приваты не официальная! Требуется замена на согласованную формулу.!!!
            int pricesub = (int) (optionsConfig.start_sub_cost +(1 * optionsConfig.region_multiplier) + (10 / (1 + Math.exp(1 * optionsConfig.region_ratio))) * Math.sqrt(numberOfPrivates * optionsConfig.subregion_cost * summSize ));


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





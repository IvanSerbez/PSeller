package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Commands.PsCommand;
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
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static PSPlugins.buyingRegions.PrivateOperations.Confirm.WithdrawalMoney;

public class PrivateOperations {

   ;

    public static boolean privatIntersectionCheck(Player p) {

        RegionDataBox data = new RegionDataBox(p);

            // проверка на пересечение региона с другими уже существующими регионами
            try {
                ProtectedRegion test = new ProtectedCuboidRegion("dummy", data.pos1, data.pos2);
                ApplicableRegionSet set = data.manager.getApplicableRegions(test);


                if (!set.getRegions().isEmpty()) {



                    return false;
                } else {return  true;}


            } catch (Exception e) {

                return false;
            }



    }

    public static boolean parentHasPaidFlag(Player p )
    {
        try {
            BuyingRegions plugin = (BuyingRegions) p.getServer().getPluginManager().getPlugin("BuyingRegions");
            ProtectedRegion parentReg = GetRegionWithMaxPriority(p);

            Boolean hasPaidFlag = parentReg.getFlag(plugin.PAID_FLAG);

            if (hasPaidFlag == null || !hasPaidFlag) {psMessages.NotFoundParentRegion(p);return false;}
            return true;
        } catch (Exception e){ System.out.println("Error not found this plugin !###! " + e);}

        return false;
    }



    public static boolean privateNameCheck(Player p, String privateName) {

       RegionDataBox data = new RegionDataBox(p);

        if (data.manager.getRegion(privateName) != null) {
            return false;
        }

        return true;
    }




    public  static boolean subPrivateIntersection(Player p)
    {
    RegionDataBox data = new RegionDataBox(p);

    // проверка на пересечение региона с другими уже существующими регионами
        try {
        ProtectedRegion test = new ProtectedCuboidRegion("dummy", data.pos1, data.pos2);
        ApplicableRegionSet set = data.manager.getApplicableRegions(test);


        ///   //////////////////////////////// проверка на овнера для суб приватов

        String regionId = "-1", regionIdPos1 = null, regionIdPos2 = null;


        List<ProtectedRegion> detectregion = new ArrayList<>();
        set.forEach(detectregion::add);

        for (int i = 0; i < detectregion.size(); i++)
        {
            var hashReg = detectregion.get(i);
            Set<UUID> ownersList = hashReg.getOwners().getUniqueIds();
            for (UUID playerUUID : ownersList)
            {

                if(playerUUID.equals(p.getUniqueId()))
                {

                    regionId = hashReg.getId();


                }
            }


        }




        if(!regionId.equals("-1"))
        {

            // проверка что субприват находится в привате игрока (проверка первой и второй точки)
            ApplicableRegionSet parentRegPos1 = data.manager.getApplicableRegions(data.pos1);
            ApplicableRegionSet parentRegPos2 = data.manager.getApplicableRegions(data.pos2);

            List<ProtectedRegion> detectPos1 = new ArrayList<>();
            parentRegPos1.forEach(detectPos1::add);
            List<ProtectedRegion> detectPos2 = new ArrayList<>();
            parentRegPos2.forEach(detectPos2::add);

            for (int i = 0; i < detectPos1.size(); i++)
            {
                var hashReg = detectPos1.get(i);

                regionIdPos1 = hashReg.getId();

            }
            for (int i = 0; i < detectPos2.size(); i++)
            {
                var hashReg = detectPos2.get(i);

                regionIdPos2 = hashReg.getId();

            }
            if (regionId.equals(regionIdPos1) && regionId.equals(regionIdPos2))
            {

                // допустимо создание суб привата
                return true;
            }


            return false;
        }


    } catch (Exception e) {return false;} return false;
    }



    public static void CreatePrivate(Player p, String privateName, Boolean isSub, BuyingRegions plugin)
    {
        RegionDataBox data = new RegionDataBox(p);
        if(!isSub) {
            if (privatIntersectionCheck(p) && privateNameCheck(p, privateName)) {


                ProtectedRegion privat = new ProtectedCuboidRegion(privateName, data.pos1, data.pos2);
                privat.getOwners().addPlayer(p.getUniqueId());
                privat.setFlag(plugin.PAID_FLAG, true);
                data.manager.addRegion(privat);

            }
        }else
        {
            if (subPrivateIntersection(p) && privateNameCheck(p, privateName)) {


                ProtectedRegion privat = new ProtectedCuboidRegion(privateName, data.pos1, data.pos2);
                privat.getOwners().addPlayer(p.getUniqueId());

                ProtectedRegion parentReg = GetRegionWithMaxPriority(p);

                if(parentReg != null) {

                    try {

                        privat.setParent(parentReg);
                    } catch (Exception e) {
                        return; /*Error NullParent*/
                    }
                    privat.setPriority(parentReg.getPriority() + 1); // поменять на динамическое определение
                    privat.setFlag(plugin.PAID_FLAG, true);
                    data.manager.addRegion(privat);
                }


            }

        }


    }


    private static ProtectedRegion GetRegionWithMaxPriority(Player p)
    {
        RegionDataBox data = new RegionDataBox(p);

        // проверка на пересечение региона с другими уже существующими регионами
        try {
            ProtectedRegion test = new ProtectedCuboidRegion("dummy", data.pos1, data.pos2);
            ApplicableRegionSet set = data.manager.getApplicableRegions(test);

            String regionId = "-1";
            ProtectedRegion maxPriorityReg = null;

            // Получение всех регионов в выделении
            List<ProtectedRegion> detectregion = new ArrayList<>();
            set.forEach(detectregion::add);

            for (int i = 0; i < detectregion.size(); i++) {
                var hashReg = detectregion.get(i);
                // получени и проверка овнеров в регионе
                Set<UUID> ownersList = hashReg.getOwners().getUniqueIds();
                for (UUID playerUUID : ownersList) {

                    if (playerUUID.equals(p.getUniqueId())) {

                        if(maxPriorityReg == null) {maxPriorityReg = hashReg;}
                        else if(maxPriorityReg.getPriority() < hashReg.getPriority())
                        {
                            maxPriorityReg = hashReg;
                        }

                        


                    }
                }
            }
            return maxPriorityReg;


            } catch (Exception e) {/*error*/}

     return null;
    }
    public static class RegionDataBox {

        public BlockVector3 pos1, pos2;
        public   World bukkitWorld;
        public   BukkitPlayer player;
        public   com.sk89q.worldedit.world.World CurrentWorld;
        public   LocalSession session;
        public  RegionContainer container;
        public  RegionManager manager;
        public  Region region;

        public RegionDataBox(Player p) {


             bukkitWorld = p.getWorld();
             player = BukkitAdapter.adapt(p);
             CurrentWorld = BukkitAdapter.adapt(bukkitWorld);
             session = WorldEdit.getInstance().getSessionManager().get(player);

            container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            manager = container.get(CurrentWorld);

            try {
                region = session.getSelection(player.getWorld());

                pos1 = region.getMinimumPoint();
                pos2 = region.getMaximumPoint();

            } catch (IncompleteRegionException e) {
           //    psMessages.NotFoundSelectionMess(p);
            }


        }


    }
}
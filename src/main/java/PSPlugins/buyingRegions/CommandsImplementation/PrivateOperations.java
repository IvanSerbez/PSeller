package PSPlugins.buyingRegions.CommandsImplementation;

import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Files.PaidRegionBirthdayDataBase;
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
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class PrivateOperations {



    /// проверка на пересечение региона с другими уже существующими регионами
    public static boolean privatIntersectionCheck(Player p) {

        RegionDataBox data = new RegionDataBox(p);

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



    /// проверка на нахождение суб региона в платном регионе игрока

    public  static boolean subPrivateIntersection(Player p)
    {
        RegionDataBox data = new RegionDataBox(p);

        try {
        ProtectedRegion test = new ProtectedCuboidRegion("dummy", data.pos1, data.pos2);
        ApplicableRegionSet set = data.manager.getApplicableRegions(test);


        /// проверка на овнера для суб приватов

        String regionId = "-1", regionIdPos1 = null, regionIdPos2 = null;


        List<ProtectedRegion> detectregion = new ArrayList<>();
        set.forEach(detectregion::add);

        ///  проверка находится ли игрок в списке овнеров во всех найденных регионах пересечения.
        for (int i = 0; i < detectregion.size(); i++)
        {
            var hashReg = detectregion.get(i);
            Set<UUID> ownersList = hashReg.getOwners().getUniqueIds();
            for (UUID playerUUID : ownersList)
            {
                if(playerUUID.equals(p.getUniqueId())) {regionId = hashReg.getId();}
            }
        }




        if(!regionId.equals("-1"))
        {

            /// проверка что субприват находится в привате игрока (проверка первой и второй точки)
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

                /// допустимо создание суб региона
                return true;
            }

            ///  вне родительского платного региона. запрещено создание суб региона
            return false;
        }


    } catch (Exception e) {return false;} return false;
    }
    ///  получение названия мира по привату.
    public static World getWorld(ProtectedRegion region)
    {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();

        for (World bukkitWorld : Bukkit.getWorlds()) {
            com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(bukkitWorld);
            RegionManager manager = container.get(weWorld);
            if (manager != null && manager.hasRegion(region.getId())) {
                return  bukkitWorld;
            }

        }
        return null;
    }

    ///  создание региона или суб региона. использовать только после снятия денег
    public static void CreatePrivate(Player p, String privateName, Boolean isSub, BuyingRegions plugin)
    {
        RegionDataBox data = new RegionDataBox(p);
        if(!isSub) {
            if (privatIntersectionCheck(p) && privateNameCheck(p, privateName)) {


                ProtectedRegion privat = new ProtectedCuboidRegion(privateName, data.pos1, data.pos2);
                privat.getOwners().addPlayer(p.getUniqueId());
                privat.setFlag(plugin.PAID_FLAG, true);
                data.manager.addRegion(privat);
                /// добавление даты создания
                String path = PaidRegionBirthdayDataBase.getPath(p.getName(),privat.getId(),data.bukkitWorld.getName());
                PaidRegionBirthdayDataBase.addString(path,GetTime());
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
                    privat.setPriority(parentReg.getPriority() + 1);
                    privat.setFlag(plugin.PAID_FLAG, true);
                    data.manager.addRegion(privat);

                }


            }

        }


    }


    /// возвращает регион с самым большим приоритетом
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

    private static String GetTime()
    {

        ZonedDateTime Msc = ZonedDateTime.now(ZoneId.of("Europe/Moscow"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String Date = Msc.format(formatter);

        return Date;
    }

    ///получение строки для поиска дат создания

    ///  Данные выделения WorldGuard     !!! Не путать с CostDataBox !!!
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

            } catch (IncompleteRegionException e) {}


        }


    }




    /// взятие списка всех ПЛАТНЫХ регионов в которых игрок является владельцем.
    /// не считая приваты у которых есть родитель
    public static Collection<ProtectedRegion> getPaidPrivates(Player p)
    {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(p.getWorld());
        RegionManager manager = container.get(weWorld);

        UUID uuid = p.getUniqueId();
        String name = p.getName();

        List<ProtectedRegion> ownedRegions = manager.getRegions().values().stream()
                .filter(region ->
                        region.getOwners().contains(uuid) || region.getOwners().contains(name)
                )
                .collect(Collectors.toList());

        BuyingRegions plugin = (BuyingRegions) p.getServer().getPluginManager().getPlugin("BuyingRegions");
        try{
            List<ProtectedRegion> paidRegions =
                    ownedRegions.stream().filter(region -> region.getFlag(plugin.PAID_FLAG) && region.getParent() == null ).collect(Collectors.toList());

            return paidRegions;
        }catch (Exception e){e.printStackTrace();  return Collections.emptyList();}
    }

}
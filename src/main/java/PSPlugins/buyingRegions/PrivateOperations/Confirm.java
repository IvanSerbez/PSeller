package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.Hooks.VaultHook;
import PSPlugins.buyingRegions.Messages.psMessages;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class Confirm {

static String privateName = null;
static  String mDataPrivateName = "PrivateName";

    public static void confirmBuying(Player p)
    {
        boolean thisIsSubPrivate = false;
        if(p.hasMetadata("ThisIsSubPrivate"))
        {thisIsSubPrivate = p.getMetadata("ThisIsSubPrivate").get(0).asBoolean(); p.sendMessage(thisIsSubPrivate + "  p.getMetadata(\"ThisIsSubPrivate\").get(0).asBoolean() "); }

        if(thisIsSubPrivate)
        {confirmSubPrivate(p);} else
        {confirmPrivate(p);}

    }

    private static void  confirmPrivate(Player p)
    {
        if(!hasMoney(p,false))
        { /* error */  return;}
        // получаем название будущего привата

       if(updatePrivateName(p))
       {
        if(PrivateOperations.privateNameCheck(p,privateName) && PrivateOperations.privatIntersectionCheck(p))
        {
          byebyeMoney(p,false);
        }
       }
    }

    private static void  confirmSubPrivate(Player p)
    {
        if(!hasMoney(p,true))
        { /* error */  return;}
        p.sendMessage(hasMoney(p,false) + "has money  sub");
        // получаем название будущего привата
        if(updatePrivateName(p))
        {
            if(PrivateOperations.privateNameCheck(p,privateName) && PrivateOperations.subPrivateIntersection(p))
            {
                byebyeMoney(p,true);
            }
        }
    }

    private static boolean updatePrivateName(Player p)
    {
        if (p.hasMetadata(mDataPrivateName))
        {privateName = p.getMetadata(mDataPrivateName).get(0).asString(); return true;} else /* Ошибка имени */ return false;
    }


    private static boolean hasMoney(Player p, Boolean isSub)
    {
        CostDataBox costDataBox = Cost.getCostDataBox(p);

        if(costDataBox == null) { /* error */ return false; }

        ///  Проверка счета
        Economy economy = VaultHook.getEconomy();
        if(!isSub) {
            if (!economy.has(p, costDataBox.price)){psMessages.PrivateNotEnoughMoney(p); return false;}
            else return true;
        }
        else
        {
            if (!economy.has(p, costDataBox.priceSubPrivate)){psMessages.PrivateNotEnoughMoney(p); return false;}
            else return true;
        }
    }


    private static void byebyeMoney(Player p, Boolean isSub) {

        CostDataBox costDataBox = Cost.getCostDataBox(p);


        if (costDataBox == null) { /* error */}


        ///  Проверка счета
        Economy economy = VaultHook.getEconomy();

        if (!isSub) {

            EconomyResponse response = economy.withdrawPlayer(p, costDataBox.price);
            if (response.transactionSuccess()) {
                PrivateOperations.CreatePrivate(p, privateName, false);

                psMessages.ByeByeMoney(p, costDataBox.price);
                psMessages.Privatebuy(p);
            } else {
                psMessages.PrivateNotEnoughMoney(p);
            }
        } else
        {

            EconomyResponse response = economy.withdrawPlayer(p, costDataBox.priceSubPrivate);
            if (response.transactionSuccess()) {
                PrivateOperations.CreatePrivate(p, privateName, true);
                psMessages.ByeByeMoney(p, costDataBox.priceSubPrivate);
                psMessages.Privatebuy(p);
            } else {
                psMessages.PrivateNotEnoughMoney(p);
            }

        }
    }
}



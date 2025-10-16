package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Hooks.VaultHook;
import PSPlugins.buyingRegions.Messages.psMessages;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class Confirm {

static String privateName = null;
static  String mDataPrivateName = "PrivateName";

    public static void confirmBuying(Player p, BuyingRegions plugin)
    {
        boolean thisIsSubPrivate = false;
        if(p.hasMetadata("ThisIsSubPrivate"))
        {thisIsSubPrivate = p.getMetadata("ThisIsSubPrivate").get(0).asBoolean();}


        if(thisIsSubPrivate)
        {confirmSubPrivate(p, plugin);} else
        {confirmPrivate(p, plugin);}

    }

    private static void  confirmPrivate(Player p, BuyingRegions plugin)
    {
        if(!hasMoney(p,false))
        { /* error */  return;}
        // получаем название будущего привата

       if(updatePrivateName(p))
       {
        if(PrivateOperations.privateNameCheck(p,privateName) && PrivateOperations.privatIntersectionCheck(p))
        {
           WithdrawalMoney(p,false, plugin);

            psMessages.WithdrawalMoney(p);
            psMessages.Privatebuy(p);

        }
       }
    }

    private static void  confirmSubPrivate(Player p, BuyingRegions plugin)
    {
        if(!PrivateOperations.parentHasPaidFlag(p)){ return;}
        if(!hasMoney(p,true))
        { psMessages.PrivateNotEnoughMoney(p);  return;}

        // получаем название будущего привата
        if(updatePrivateName(p))
        {
            if(PrivateOperations.privateNameCheck(p,privateName) && PrivateOperations.subPrivateIntersection(p))
            {
                WithdrawalMoney(p,true, plugin);

                psMessages.WithdrawalMoneySub(p);
                psMessages.Privatebuy(p);

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

        if(costDataBox == null) { /* error */psMessages.NotFoundSelectionMess(p); return false; }

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


    public static void WithdrawalMoney(Player p, Boolean isSub, BuyingRegions plugin) {

        CostDataBox costDataBox = Cost.getCostDataBox(p);


        if (costDataBox == null) { return;/* error */}


        ///  Проверка счета
        Economy economy = VaultHook.getEconomy();

        if (!isSub) {

            EconomyResponse response = economy.withdrawPlayer(p, costDataBox.price);
            if (response.transactionSuccess()) {
                PrivateOperations.CreatePrivate(p, privateName, false, plugin);

            } else {
                psMessages.PrivateNotEnoughMoney(p);
            }
        } else
        {

            EconomyResponse response = economy.withdrawPlayer(p, costDataBox.priceSubPrivate);
            if (response.transactionSuccess()) {
                PrivateOperations.CreatePrivate(p, privateName, true, plugin);
             //   psMessages.WithdrawalMoneySub(p);
             //   psMessages.Privatebuy(p);
            } else {
                psMessages.PrivateNotEnoughMoney(p);
            }

        }
    }
}



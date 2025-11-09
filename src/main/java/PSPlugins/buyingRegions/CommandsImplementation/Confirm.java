package PSPlugins.buyingRegions.CommandsImplementation;

import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Files.GetOptionsConfig;
import PSPlugins.buyingRegions.Hooks.VaultHook;
import PSPlugins.buyingRegions.Messages.psMessages;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class Confirm {

static String privateName = null;
static  String mDataPrivateName = "PrivateName";


    ///  ps confirm определяет какой регион регистрируется. суб регион или обычный регион
    public static void confirmBuying(Player p, BuyingRegions plugin)
    {
        /// взятие данных выделения
       CostDataBox costDataBox = Cost.getCostDataBox(p);

        GetOptionsConfig optionsConfig = new GetOptionsConfig();
       if (costDataBox == null) { psMessages.NotFoundSelectionMess(p);} else {
           if(costDataBox.summSize > optionsConfig.region_volume_max || costDataBox.summSize < optionsConfig.region_volume_min){ psMessages.ErrorLimitOfBlocks(p); return; }
           boolean thisIsSubPrivate = false;

           /// взятие кэша типа региона
           if (p.hasMetadata("ThisIsSubPrivate")) {
               thisIsSubPrivate = p.getMetadata("ThisIsSubPrivate").get(0).asBoolean();
           }


           if (thisIsSubPrivate) {
               confirmSubPrivate(p, plugin);
           } else {
               confirmPrivate(p, plugin);
           }
       }
    }

    ///  подтверждение  покупки региона. снимает деньги и создает регион
    private static void  confirmPrivate(Player p, BuyingRegions plugin)
    {
        if(!hasMoney(p,false))
        { /* error */  return;}

        /// проверка имени
       if(updatePrivateName(p))
       {
           /// проверка на имя и пересечение - после снятие денег и создание региона
        if(PrivateOperations.privateNameCheck(p,privateName) && PrivateOperations.privatIntersectionCheck(p))
        {
           WithdrawalMoney(p,false, plugin);

            psMessages.WithdrawalMoney(p);
            psMessages.Privatebuy(p);

        }
       }
    }

    ///  подтверждение  покупки Суб-региона. снимает деньги и создает Суб-регион
    private static void  confirmSubPrivate(Player p, BuyingRegions plugin)
    {
        if(!PrivateOperations.parentHasPaidFlag(p)){ return;}
        if(!hasMoney(p,true))
        { psMessages.PrivateNotEnoughMoney(p);  return;}

        /// проверка имени
        if(updatePrivateName(p))
        {  /// проверка на имя и проверка родителя - после снятие денег и создание суб-региона
            if(PrivateOperations.privateNameCheck(p,privateName) && PrivateOperations.subPrivateIntersection(p))
            {
                WithdrawalMoney(p,true, plugin);

                psMessages.WithdrawalMoneySub(p);
                psMessages.Privatebuy(p);

            }
        }
    }

    /// взятие имени региона. проверка есть ли имя
    private static boolean updatePrivateName(Player p)
    {
        if (p.hasMetadata(mDataPrivateName))
        {privateName = p.getMetadata(mDataPrivateName).get(0).asString(); return true;} else  return false;
    }

    /// проверка баланса игрока
    private static boolean hasMoney(Player p, Boolean isSub)
    {
        /// взятие данных выделения
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

    ///  снятие денег со счета игрока. в дальнейшем подтверждает создание региона!
    public static void WithdrawalMoney(Player p, Boolean isSub, BuyingRegions plugin) {

        ///  взятие данных выделения
        CostDataBox costDataBox = Cost.getCostDataBox(p);


        if (costDataBox == null) { return;/* error */}


        Economy economy = VaultHook.getEconomy();

        if (!isSub) {
            ///  снятие денег и создание региона
            EconomyResponse response = economy.withdrawPlayer(p, costDataBox.price);
            if (response.transactionSuccess()) {
                PrivateOperations.CreatePrivate(p, privateName, false, plugin);

            } else {
                psMessages.PrivateNotEnoughMoney(p);
            }
        } else
        {
            ///  снятие денег и создание Суб региона
            EconomyResponse response = economy.withdrawPlayer(p, costDataBox.priceSubPrivate);
            if (response.transactionSuccess()) {
                PrivateOperations.CreatePrivate(p, privateName, true, plugin);

            } else {
                psMessages.PrivateNotEnoughMoney(p);
            }

        }
    }
}



package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Hooks.VaultHook;
import PSPlugins.buyingRegions.Messages.psMessages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class rgSub {

    public static  void buySubPrivate(Player p, String privateName, BuyingRegions plugin)
    {


        CostDataBox costDatabox = Cost.getCostDataBox(p);
        if(costDatabox == null) {return;}



        if(!PrivateOperations.privateNameCheck(p,privateName))
        {

            psMessages.PrivateNameErrorMess(p,privateName);
            return;
        }else
        {     p.setMetadata("privateName", new FixedMetadataValue(plugin, privateName));
        }


        if(PrivateOperations.subPrivateIntersection(p))
        {
            if (costDatabox != null) {
                Economy economy = VaultHook.getEconomy();
                if (!economy.has(p, costDatabox.priceSubPrivate)) {
                    psMessages.PrivateNotEnoughMoney(p);
                    return;


                } else {
                    p.setMetadata("ThisIsSubPrivate", new FixedMetadataValue(plugin, true));
                    psMessages.PrivatePriceMess(p, Cost.getCostDataBox(p).priceSubPrivate);
                }

            }

        }

        if(PrivateOperations.privatIntersectionCheck(p)) {


            if (costDatabox != null) {
                Economy economy = VaultHook.getEconomy();
                if (!economy.has(p, costDatabox.price)) {
                    psMessages.PrivateNotEnoughMoney(p);
                    return;


                } else {
                    psMessages.PrivatePriceMess(p, Cost.getCostDataBox(p).price);
                }

            }
        }  else {psMessages.PrivateAreaErrorMess(p);}




    }
    public static  void buySubPrivate(Player p){}
}

package PSPlugins.buyingRegions.PrivateOperations;


import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Hooks.VaultHook;
import PSPlugins.buyingRegions.Messages.psMessages;

import jdk.jfr.Enabled;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;


public class rgBuy {




    public static void buyRegion(Player p, String privateName, BuyingRegions plugin) {



        if(!PrivateOperations.privateNameCheck(p,privateName))
        {
            psMessages.PrivateNameErrorMess(p,privateName);
        }else
        {     p.setMetadata("privateName", new FixedMetadataValue(plugin, privateName));
        }

        if(PrivateOperations.privatIntersectionCheck(p))
        {
            CostDataBox costDatabox = Cost.getCostDataBox(p);
            Economy economy = VaultHook.getEconomy();
            if(!economy.has(p,costDatabox.price))
            {
                psMessages.PrivateNotEnoughMoney(p);


            } else {
            psMessages.PrivatePriceMess(p, Cost.getCostDataBox(p).price); }
        } else {psMessages.PrivateAreaErrorMess(p);}




                // cоздание надо перенести в confirm после подтверждения платежа
                // надо проверять пересечения регионов после подтверждения. но до снятия денег и создания региона

    }

    public static void buyRegion(Player p){

        psMessages.NotFoundNamePrivateMess(p);

    }

}





package PSPlugins.buyingRegions.PrivateOperations;


import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Hooks.VaultHook;
import PSPlugins.buyingRegions.Messages.psMessages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class rgBuy {

    static  String mDataPrivateName = "PrivateName";



    public static void buyRegion(Player p, String privateName, BuyingRegions plugin) {

        CostDataBox costDatabox = Cost.getCostDataBox(p);
        if(costDatabox == null) {return;}
        p.setMetadata("ThisIsSubPrivate", new FixedMetadataValue(plugin, false));

        String name = privateName;
        Pattern pattern = Pattern.compile("\\W");
        Matcher matcher = pattern.matcher(name);
        if(matcher.find())
        { // ошибка имени. неверный формат имени
           psMessages.PrivateNameErrorRegEx(p);
            return;
        }


        p.setMetadata(mDataPrivateName, new FixedMetadataValue(plugin, privateName));
        if(!PrivateOperations.privateNameCheck(p,privateName)) {psMessages.PrivateNameErrorMess(p);return;}


        if(PrivateOperations.privatIntersectionCheck(p)) {


            if (costDatabox != null) {
                Economy economy = VaultHook.getEconomy();
                if (!economy.has(p, costDatabox.price)) {
                    psMessages.PrivateNotEnoughMoney(p);
                }
                else {psMessages.PrivatePriceMess(p);}

            }
        }  else {psMessages.PrivateAreaErrorMess(p);}


    }

    public static void buyRegion(Player p){

        psMessages.NotFoundNamePrivateMess(p);

    }

}





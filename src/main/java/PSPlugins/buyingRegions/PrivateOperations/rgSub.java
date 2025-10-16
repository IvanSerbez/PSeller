package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Hooks.VaultHook;
import PSPlugins.buyingRegions.Messages.psMessages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class rgSub {
    static  String mDataPrivateName = "PrivateName";

    public static  void buySubPrivate(Player p, String privateName, BuyingRegions plugin)
    {

        p.setMetadata("ThisIsSubPrivate", new FixedMetadataValue(plugin, false));

        CostDataBox costDatabox = Cost.getCostDataBox(p);
        if(costDatabox == null) {return;}

        String name = privateName;
        Pattern pattern = Pattern.compile("\\W");
        Matcher matcher = pattern.matcher(name);
        if(matcher.find())
        { // ошибка имени. неверный формат имени
            psMessages.PrivateNameErrorRegEx(p);
            return;
        }

        p.setMetadata(mDataPrivateName, new FixedMetadataValue(plugin, privateName));
        if(!PrivateOperations.privateNameCheck(p,privateName))  {psMessages.PrivateNameErrorMess(p);return;}



        if(PrivateOperations.subPrivateIntersection(p))
        {
                Economy economy = VaultHook.getEconomy();
                if (!economy.has(p, costDatabox.priceSubPrivate)) {
                    psMessages.PrivateNotEnoughMoney(p);

                } else {
                    p.setMetadata("ThisIsSubPrivate", new FixedMetadataValue(plugin, true));
                    psMessages.PrivatePriceMessSub(p);
                }



        }else {psMessages.NotFoundParentRegion(p);}

    }
    public static  void buySubPrivate(Player p){ psMessages.NotFoundNamePrivateMessSub(p);/* Ошибка. Введите название суб привата */}
}

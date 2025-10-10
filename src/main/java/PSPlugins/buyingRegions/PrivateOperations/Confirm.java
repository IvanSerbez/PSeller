package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.Hooks.VaultHook;
import PSPlugins.buyingRegions.Messages.psMessages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class Confirm {



    public static void confrimBuying(Player p)
    {
        CostDataBox costDatabox = Cost.getCostDataBox(p);
        Economy economy = VaultHook.getEconomy();
        if(!economy.has(p,costDatabox.price))
        {
            psMessages.PrivateNotEnoughMoney(p);


        } else
        {


        }


    }
}

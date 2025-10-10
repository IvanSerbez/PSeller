package PSPlugins.buyingRegions.PrivateOperations;

import PSPlugins.buyingRegions.Hooks.VaultHook;
import PSPlugins.buyingRegions.Messages.psMessages;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class Confirm {



    public static void confrimBuying(Player p)
    {
        CostDataBox costDatabox = Cost.getCostDataBox(p);
        Economy economy = VaultHook.getEconomy();
        if(costDatabox != null) {
            if (!economy.has(p, costDatabox.price)) {
                psMessages.PrivateNotEnoughMoney(p);


            } else {
                String privateName;

                if (p.hasMetadata("privateName")) {
                    privateName = p.getMetadata("privateName").get(0).asString();

                    if (PrivateOperations.privateNameCheck(p, privateName) && PrivateOperations.privatIntersectionCheck(p)) {

                        EconomyResponse response = economy.withdrawPlayer(p, costDatabox.price);
                        if (response.transactionSuccess()) {
                            PrivateOperations.CreatePrivate(p, privateName);
                            psMessages.ByeByeMoney(p, costDatabox.price);
                            psMessages.Privatebuy(p);
                        } else {
                            psMessages.PrivateNotEnoughMoney(p);
                        }
                    }
                } else {
                    // Название привата не найдено или занято
                }


            }

        } else { //psMessages.NotFoundSelectionMess(p);
             }

    }
}

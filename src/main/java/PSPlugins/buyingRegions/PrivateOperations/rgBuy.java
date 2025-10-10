package PSPlugins.buyingRegions.PrivateOperations;


import PSPlugins.buyingRegions.Messages.psMessages;

import org.bukkit.entity.Player;


public class rgBuy {


    public static void buyRegion(Player p, String privateName) {


        if(!PrivateOperations.privateNameCheck(p,privateName))
        {
            psMessages.PrivateNameErrorMess(p,privateName);
        }

        if(PrivateOperations.privatIntersectionCheck(p))
        {
            psMessages.PrivatePriceMess(p, Cost.getCostDataBox(p).price);
        } else {psMessages.PrivateAreaErrorMess(p);}



                // cоздание надо перенести в confirm после подтверждения платежа
                // надо проверять пересечения регионов после подтверждения. но до снятия денег и создания региона

    }

    public static void buyRegion(Player p){

        psMessages.NotFoundNamePrivateMess(p);

    }

}





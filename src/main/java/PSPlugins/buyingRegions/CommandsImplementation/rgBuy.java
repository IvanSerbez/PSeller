package PSPlugins.buyingRegions.CommandsImplementation;


import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.Files.GetOptionsConfig;
import PSPlugins.buyingRegions.Hooks.VaultHook;
import PSPlugins.buyingRegions.Messages.psMessages;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class rgBuy {

    static  String mDataPrivateName = "PrivateName";


    ///  назначает тип и имя региона. после предлагает подтвердить покупку
    public static void buyRegion(Player p, String privateName, BuyingRegions plugin) {

        GetOptionsConfig optionsConfig = new GetOptionsConfig();

        CostDataBox costDatabox = Cost.getCostDataBox(p);
        if(costDatabox == null) { psMessages.NotFoundSelectionMess(p);  return; }
        p.setMetadata("ThisIsSubPrivate", new FixedMetadataValue(plugin, false));



        ///  Модуль на проверку формата имени региона перед регистрацией
        /// ///////////////////////////////////////////////
        String name = privateName;
        Pattern pattern = Pattern.compile("\\W");
        Matcher matcher = pattern.matcher(name);
        if(matcher.find())
        { // ошибка имени. неверный формат имени
           psMessages.PrivateNameErrorRegEx(p);
            return;
        }
        /// /////////////////////////////////////////////

        p.setMetadata(mDataPrivateName, new FixedMetadataValue(plugin, privateName));

        /// Проверка на название региона. если уже есть регион с таким именем - закончит процесс метода ошибкой
        if(!PrivateOperations.privateNameCheck(p,privateName)) {psMessages.PrivateNameErrorMess(p);return;}

        ///  Проверка на пересечения регионов
        if(PrivateOperations.privatIntersectionCheck(p)) {

            if (costDatabox.summSize > optionsConfig.region_volume_max || costDatabox.summSize < optionsConfig.region_volume_min) {psMessages.ErrorLimitOfBlocks(p); return;}
            ///  Проверка баланса игрока
            if (costDatabox != null) {
                Economy economy = VaultHook.getEconomy();
                if (!economy.has(p, costDatabox.price)) {
                    psMessages.PrivateNotEnoughMoney(p);
                }
                else
                {psMessages.PrivatePriceMess(p);}

            }
        }  else {psMessages.PrivateAreaErrorMess(p);}


    }

    ///  ошибка ввод команды без имени суб региона
    public static void buyRegion(Player p){

        psMessages.NotFoundNamePrivateMess(p);

    }

}





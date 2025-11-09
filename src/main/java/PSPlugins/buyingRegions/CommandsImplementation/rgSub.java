package PSPlugins.buyingRegions.CommandsImplementation;

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

    ///  назначает тип и имя суб-региона. после предлагает подтвердить покупку
    public static  void buySubPrivate(Player p, String privateName, BuyingRegions plugin)
    {

        ///  Кэш донные для comfirm суб/не суб региона
        p.setMetadata("ThisIsSubPrivate", new FixedMetadataValue(plugin, false));

        /// получение данных выделения
        CostDataBox costDatabox = Cost.getCostDataBox(p);
        if(costDatabox == null) {psMessages.NotFoundSelectionMess(p);  return;}


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
        /// ///////////////////////////////////////////////

        ///  Кэш данные названия региона
        p.setMetadata(mDataPrivateName, new FixedMetadataValue(plugin, privateName));
        ///  проверка на занятость названия
        if(!PrivateOperations.privateNameCheck(p,privateName))  {psMessages.PrivateNameErrorMess(p);return;}


        ///  проверка нахождения суб привата полностью в платном регионе игрока
        if(PrivateOperations.subPrivateIntersection(p))
        {
            ///  проверка баланса игрока
                Economy economy = VaultHook.getEconomy();
                if (!economy.has(p, costDatabox.priceSubPrivate)) {
                    psMessages.PrivateNotEnoughMoney(p);

                } else {
                    ///  установка Кэша о том. что регистрируется суб-приват
                    p.setMetadata("ThisIsSubPrivate", new FixedMetadataValue(plugin, true));
                    psMessages.PrivatePriceMessSub(p);
                }



        }else {psMessages.NotFoundParentRegion(p);}

    }

    ///  сообщение при пустом вводе /ps rgsub
    public static  void buySubPrivate(Player p){ psMessages.NotFoundNamePrivateMessSub(p);}
}

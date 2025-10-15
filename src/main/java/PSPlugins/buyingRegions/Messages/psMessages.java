package PSPlugins.buyingRegions.Messages;
import PSPlugins.buyingRegions.Files.MessagesConfig;
import PSPlugins.buyingRegions.PrivateOperations.Cost;
import PSPlugins.buyingRegions.PrivateOperations.CostDataBox;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import  org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class psMessages {


    static GetMessage message = new GetMessage();


    private static Map<String,String> GetPlaceHolders(Player p)
    {
        CostDataBox data = Cost.getCostDataBox(p);
        Map<String,String> PlaceHolders = new HashMap<>();

            PlaceHolders.put("%PLAYER_NAME%", String.valueOf(p.getDisplayName()));

        if(p.hasMetadata("PrivateName"))
        {PlaceHolders.put("%REGION_NAME%",String.valueOf(p.getMetadata("PrivateName")));}

        if(data != null) {
            PlaceHolders.put("%X_SIZE%", String.valueOf(data.size.getX()));
            PlaceHolders.put("%Y_SIZE%", String.valueOf(data.size.getY()));
            PlaceHolders.put("%Z_SIZE%", String.valueOf(data.size.getZ()));
            PlaceHolders.put("%SUMM_SIZE%",String.valueOf(data.summSize));
            PlaceHolders.put("%PRICE%",String.valueOf(data.price));
            PlaceHolders.put("%PRICE_SUB%",String.valueOf(data.priceSubPrivate));

        }
        return PlaceHolders;
    }


    private static String formatMessage(String mess, Player p)
    {
        Map<String,String> placeHolders = GetPlaceHolders(p);
        for(Map.Entry<String,String> entry : placeHolders.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();

            if(mess.contains(key))
            {
                mess = mess.replace(key,value);
            }

        }

            return ChatColor.translateAlternateColorCodes('&', mess);
    }


//Старые методы сообщений


       // static String PSStr = ChatColor.translateAlternateColorCodes('&', "&8&l[&6&lPS&8&l] ");


    public static void CostMess(Player p, int summSize)
    {
        if(summSize > 20000000)
        {
            p.sendMessage(formatMessage(message.messErrorLimitOfBlocks,p));
            //p.sendMessage( PSStr + ChatColor.RED + "Размер региона превышает лимит в 20.000.000 блоков. вы НЕ сможете приобрести такой регион!!!");
        }
            p.sendMessage(formatMessage(message.messSizeRegionXYZ,p));
            p.sendMessage(formatMessage(message.messSummRegionSize,p));
            p.sendMessage(formatMessage(message.messPrice,p));
       // p.sendMessage(PSStr
        //        + ChatColor.GRAY + "Размер региона " + ChatColor.RED +  "" + ChatColor.BOLD +"X"
        //        + ChatColor.GREEN + "" + ChatColor.BOLD + "Y"
        //        + ChatColor.BLUE + "" + ChatColor.BOLD + "Z"
        //        + ChatColor.GRAY + " : " + ChatColor.GREEN + (int)size.getX() + " " + (int)size.getY() + " " + (int)size.getZ());
      //  p.sendMessage(PSStr + ChatColor.GRAY + "Суммарное кол-во. блоков составляет : " + ChatColor.GREEN + summSize);

       // p.sendMessage(PSStr + ChatColor.GRAY + "Стоимость региона составляет : " + ChatColor.GREEN + "" + ChatColor.BOLD + price + ChatColor.GRAY + "$");

    }

    public  static void SizeMess(Player p, int summSize)
    {

        if(summSize > 20000000)
        {
            p.sendMessage(formatMessage(message.messErrorLimitOfBlocks,p));
        }

        p.sendMessage(formatMessage(message.messSummRegionSize,p));
        //  p.sendMessage(PSStr + ChatColor.GRAY + "Суммарное кол-во. блоков составляет : " + ChatColor.GREEN + summSize);

        p.sendMessage(formatMessage(message.messSizeRegionXYZ,p));
        //p.sendMessage(PSStr
          //      + ChatColor.GRAY + "Размер региона " + ChatColor.RED +  "" + ChatColor.BOLD +"X"
            //    + ChatColor.GREEN + "" + ChatColor.BOLD + "Y"
              //  + ChatColor.BLUE + "" + ChatColor.BOLD + "Z"
                //+ ChatColor.GRAY + " : " + ChatColor.GREEN + (int)size.getX() + " " + (int)size.getY() + " " + (int)size.getZ());


    }

    public static void  NotFoundParentRegion(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundParent,p));
    }

    public  static void NotFoundSelectionMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundSelection,p));
      //  p.sendMessage(PSStr
        //    + ChatColor.RED + "Выделение не найдено! выделите две точки //pos1 и //pos2");
    }

    public static void NotFoundNamePrivateMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundName,p));
      //  p.sendMessage(PSStr +
      //          ChatColor.RED + "Укажите название региона!" + ChatColor.GRAY + "   /ps rgbuy [название]" );
    }
    public static void NotFoundNamePrivateMessSub(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundNameSub,p));
        //  p.sendMessage(PSStr +
        //          ChatColor.RED + "Укажите название региона!" + ChatColor.GRAY + "   /ps rgbuy [название]" );
    }

    public static void PrivateNameErrorMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorName,p));
      //  p.sendMessage(PSStr +
         //       ChatColor.RED + "Название "+ ChatColor.GREEN + ChatColor.BOLD + privateName + ChatColor.RED +" уже занято! выберите другое название региона.");

    }

    public static void PrivateAreaErrorMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorAreaIntersection,p));
      //  p.sendMessage(PSStr +
         //       ChatColor.RED + "Здесь уже есть регион! выберите другое место для региона.");

    }

    public static void PrivatePriceMess(Player p)
    {
        p.sendMessage(formatMessage(message.messPriceConfirm,p));
        // p.sendMessage(PSStr + ChatColor.GRAY +"Покупка такого региона будет стоить " + ChatColor.GREEN + ChatColor.BOLD + price + ChatColor.GRAY + ChatColor.BOLD + "$" +  ChatColor.GRAY +", введите " + ChatColor.GREEN + "/ps confirm" + ChatColor.GRAY + " для подтверждения.");

    }

    public static void PrivatePriceMessSub(Player p)
    {
        p.sendMessage(formatMessage(message.messPriceSubConfirm,p));
        // p.sendMessage(PSStr + ChatColor.GRAY +"Покупка такого региона будет стоить " + ChatColor.GREEN + ChatColor.BOLD + price + ChatColor.GRAY + ChatColor.BOLD + "$" +  ChatColor.GRAY +", введите " + ChatColor.GREEN + "/ps confirm" + ChatColor.GRAY + " для подтверждения.");

    }

    public static  void PrivateNotEnoughMoney(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotEnoughMoney,p));
        // p.sendMessage(PSStr + ChatColor.RED + "Недостаточно средств.");

    }

    public static void Privatebuy(Player p)
    {
        p.sendMessage(formatMessage(message.messPrivateBought,p));
        //p.sendMessage(PSStr + ChatColor.GRAY + "Вы успешно приобрели регион!");
    }
    public static void WithdrawalMoney(Player p)
    {
        p.sendMessage(formatMessage(message.messWithdrawal,p));
       // p.sendMessage(PSStr + ChatColor.GRAY + "С вашего баланса снято : " + ChatColor.GREEN + price + ChatColor.GRAY+ "$");

    }
    public  static void  WithdrawalMoneySub(Player p)
    {
        p.sendMessage(formatMessage(message.messWithdrawalSub,p));
    }

    private static class GetMessage
    {


        String messHeader;
        String messSizeRegionXYZ;
        String messSummRegionSize;
        String messPrivateBought;
        String messPrice;
        String messPriceSub;
        String messPriceConfirm;
        String messPriceSubConfirm;
        String messWithdrawal;
        String messWithdrawalSub;
        String messErrorNotFoundSelection;
        String messErrorNotFoundName;
        String messErrorNotFoundNameSub;
        String messErrorAreaIntersection;
        String messErrorNotEnoughMoney;
        String messErrorLimitOfBlocks;
        String messErrorName;
        String messErrorNotFoundParent;

        // дастать из конфига все сообщения

        GetMessage()
        {
           FileConfiguration config = MessagesConfig.get();

           messHeader = config.getString("MessHeader");
           messSizeRegionXYZ = messHeader + config.getString("MessSizeRegionXYZ");
           messSummRegionSize = messHeader + config.getString("MessSummRegionSize");
           messPrivateBought = messHeader + config.getString("MessPrivateBought");
           messPrice = messHeader + config.getString("MessPrice");
           messPriceSub = messHeader + config.getString("MessPriceSub");
           messPriceConfirm = messHeader + config.getString("MessPriceConfrim");
           messPriceSubConfirm = messHeader + config.getString("MessPriceSubConfirm");
           messWithdrawal = messHeader + config.getString("MessWithdrawal");
           messWithdrawalSub = messHeader + config.getString("MessWithdrawalSub");
           messErrorNotFoundSelection = messHeader + config.getString("MessErrorNotFoundSelection");
           messErrorNotFoundName = messHeader + config.getString("MessErrorNotFoundName");
           messErrorAreaIntersection = messHeader + config.getString("MessErrorAreaIntersection");
           messErrorNotEnoughMoney = messHeader + config.getString("MessErrorNotEnoughMoney");
           messErrorLimitOfBlocks = messHeader + config.getString("MessErrorLimitOfBlocks");
           messErrorName = messHeader + config.getString("MessErrorName");
           messErrorNotFoundParent = messHeader + config.getString("MessErrorNotFoundParent");
           messErrorNotFoundNameSub = messHeader + config.getString("MessErrorNotFoundNameSub");

        }
    }
}

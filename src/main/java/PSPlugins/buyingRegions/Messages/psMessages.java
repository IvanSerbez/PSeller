package PSPlugins.buyingRegions.Messages;
import PSPlugins.buyingRegions.Files.MessagesConfig;
import PSPlugins.buyingRegions.PrivateOperations.Cost;
import PSPlugins.buyingRegions.PrivateOperations.CostDataBox;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.Map;

public class psMessages {

    static  String mDataPrivateName = "PrivateName";
    static GetMessage message = new GetMessage();


    private static Map<String,String> GetPlaceHolders(Player p)
    {
        CostDataBox data = Cost.getCostDataBox(p);
        Map<String,String> PlaceHolders = new HashMap<>();

            PlaceHolders.put("%PLAYER_NAME%", String.valueOf(p.getDisplayName()));

        if(p.hasMetadata(mDataPrivateName))
        {   var name = p.getMetadata(mDataPrivateName).get(0).asString();
            PlaceHolders.put("%REGION_NANE%", name);}


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
        CostDataBox data = Cost.getCostDataBox(p);
        if(data !=null){
        for(Map.Entry<String,String> entry : placeHolders.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();

            if(mess.contains(key))
            {
                mess = mess.replace(key,value);
            }

        }
        }

            return ChatColor.translateAlternateColorCodes('&', mess);
    }





    public static void CostMess(Player p, int summSize)
    {
        if(summSize > 20000000)
        {
            p.sendMessage(formatMessage(message.messErrorLimitOfBlocks,p));

        }
            p.sendMessage(formatMessage(message.messSummRegionSize,p));
            p.sendMessage(formatMessage(message.messSizeRegionXYZ,p));
            p.sendMessage(formatMessage(message.messPrice,p));

    }

    public  static void SizeMess(Player p, int summSize)
    {

        if(summSize > 20000000)
        {
            p.sendMessage(formatMessage(message.messErrorLimitOfBlocks,p));
        }

        p.sendMessage(formatMessage(message.messSummRegionSize,p));

        p.sendMessage(formatMessage(message.messSizeRegionXYZ,p));
    }

    public static void  NotFoundParentRegion(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundParent,p));
    }

    public  static void NotFoundSelectionMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundSelection,p));

    }

    public static void NotFoundNamePrivateMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundName,p));

    }
    public static void NotFoundNamePrivateMessSub(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundNameSub,p));

    }

    public static void PrivateNameErrorMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorName,p));

    }

    public static void PrivateAreaErrorMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorAreaIntersection,p));

    }

    public static void PrivatePriceMess(Player p)
    {
        p.sendMessage(formatMessage(message.messPriceConfirm,p));

    }

    public static void PrivatePriceMessSub(Player p)
    {
        p.sendMessage(formatMessage(message.messPriceSubConfirm,p));

    }

    public static  void PrivateNotEnoughMoney(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotEnoughMoney,p));

    }

    public static void Privatebuy(Player p)
    {
        p.sendMessage(formatMessage(message.messPrivateBought,p));

    }
    public static void WithdrawalMoney(Player p)
    {
        p.sendMessage(formatMessage(message.messWithdrawal,p));

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

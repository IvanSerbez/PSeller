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

    ///  ссылка на кэш
    static  String mDataPrivateName = "PrivateName";
    /// получение заготовленных сообщений
    static GetMessage message = new GetMessage();


    ///  замена плейсхолдеров ( %ABC% ) на нужные данные
    private static Map<String,String> GetPlaceHolders(Player p)
    {
        CostDataBox data = Cost.getCostDataBox(p);
        Map<String,String> PlaceHolders = new HashMap<>();

            PlaceHolders.put("%PLAYER_NAME%", String.valueOf(p.getDisplayName()));

        if(p.hasMetadata(mDataPrivateName))
        {   var name = p.getMetadata(mDataPrivateName).get(0).asString();
            PlaceHolders.put("%REGION_NANE%", name);}


        if(data != null) {
            PlaceHolders.put("%X_SIZE%", String.valueOf((int)data.size.getX()));
            PlaceHolders.put("%Y_SIZE%", String.valueOf((int)data.size.getY()));
            PlaceHolders.put("%Z_SIZE%", String.valueOf((int)data.size.getZ()));
            PlaceHolders.put("%SUMM_SIZE%",String.valueOf(data.summSize));
            PlaceHolders.put("%PRICE%",String.valueOf(data.price));
            PlaceHolders.put("%PRICE_SUB%",String.valueOf(data.priceSubPrivate));

        }
        return PlaceHolders;
    }

    ///  форматирует заготовленные сообщения в формат сообщений для чата игры (цвет, стиль)
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



    ///  ошибка. выделение больше лимита блоков
    public  static  void ErrorLimitOfBlocks(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorLimitOfBlocks,p));
    }

    ///  сообщения подсчета выделения
    public static void CostMess(Player p, int summSize)
    {
        if(summSize > 20000000 || summSize < 0)
        {
            ErrorLimitOfBlocks(p);
        }
            p.sendMessage(formatMessage(message.messSummRegionSize,p));
            p.sendMessage(formatMessage(message.messSizeRegionXYZ,p));
            p.sendMessage(formatMessage(message.messPrice,p));

    }

    ///  сообщения подсчета выделения без цены
    public  static void SizeMess(Player p, int summSize)
    {

        if(summSize > 20000000 || summSize < 0)
        {
            p.sendMessage(formatMessage(message.messErrorLimitOfBlocks,p));

        }

        p.sendMessage(formatMessage(message.messSummRegionSize,p));

        p.sendMessage(formatMessage(message.messSizeRegionXYZ,p));
    }

    ///  сообщение. суб приват не находится в платном привате игрока
    public static void  NotFoundParentRegion(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundParent,p));
    }

    ///  сообщение. не найдено выделение
    public  static void NotFoundSelectionMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundSelection,p));

    }

    /// сообщение. не найдено название для региона
    public static void NotFoundNamePrivateMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundName,p));

    }
    /// сообщение. не найдено название для Суб-региона
    public static void NotFoundNamePrivateMessSub(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotFoundNameSub,p));

    }

    ///  Сообщение. Имя региона уже занято
    public static void PrivateNameErrorMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorName,p));

    }

    ///  Сообщение. не верный формат имени региона
    public static void PrivateNameErrorRegEx(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNameRegEx,p));
    }

    ///  сообщение. выделение перекрывает другие регионы.
    public static void PrivateAreaErrorMess(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorAreaIntersection,p));

    }

    ///  сообщение со стоимостью региона и предложением ввести подтверждение
    public static void PrivatePriceMess(Player p)
    {
        p.sendMessage(formatMessage(message.messPriceConfirm,p));

    }
    ///  сообщение со стоимостью Суб-региона и предложением ввести подтверждение
    public static void PrivatePriceMessSub(Player p)
    {
        p.sendMessage(formatMessage(message.messPriceSubConfirm,p));

    }

    ///  Сообщение. недостаточно денег для покупки региона
    public static  void PrivateNotEnoughMoney(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorNotEnoughMoney,p));

    }

    ///  Сообщение об покупке региона
    public static void Privatebuy(Player p)
    {
        p.sendMessage(formatMessage(message.messPrivateBought,p));

    }

    ///  Сообщение. снятие денег со счета
    public static void WithdrawalMoney(Player p)
    {
        p.sendMessage(formatMessage(message.messWithdrawal,p));

    }

    ///  Сообщение. снятие денег со счета со стоимостью суб региона
    public  static void  WithdrawalMoneySub(Player p)
    {
        p.sendMessage(formatMessage(message.messWithdrawalSub,p));
    }


    ///  взятие сообщений из конфига
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
        String messErrorNameRegEx;

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
           messErrorNameRegEx = messHeader + config.getString("MessErrorNameRegEx");
        }
    }
}

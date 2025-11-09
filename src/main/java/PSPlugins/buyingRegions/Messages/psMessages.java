package PSPlugins.buyingRegions.Messages;
import PSPlugins.buyingRegions.CommandsImplementation.PrivateOperations;
import PSPlugins.buyingRegions.Files.MessagesConfig;
import PSPlugins.buyingRegions.CommandsImplementation.Cost;
import PSPlugins.buyingRegions.CommandsImplementation.CostDataBox;
import PSPlugins.buyingRegions.Files.PaidRegionBirthdayDataBase;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class psMessages {

    ///  ссылка на кэш
    static  String mDataPrivateName = "PrivateName";
    /// получение заготовленных сообщений
    static GetMessage message = new GetMessage();


    ///  замена плейсхолдеров ( %ABC% ) на нужные данные
    private static Map<String,String> GetPlaceHolders(Player p)
    {

        GetMessage messAndStyle = new GetMessage();
        CostDataBox data = Cost.getCostDataBox(p);
        Map<String,String> PlaceHolders = new HashMap<>();

        PlaceHolders.put("%PLAYER_NAME%", String.valueOf(p.getDisplayName()));

        PlaceHolders.put("%PageNextButton%", String.valueOf(messAndStyle.styleButtonPageNext));
        PlaceHolders.put("%PagePreviousButton%", String.valueOf(messAndStyle.styleButtonPagePrevious));
        PlaceHolders.put("%PaidPrivateInfoButton%",String.valueOf(messAndStyle.styleButtonInfo));


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

    /// сбор всех имен платных регионов и их даты создания.
    private static Map<String,String> getRgListPlaceholders(Player p){
        ///  сбор значений для Ps List
        Collection<ProtectedRegion> regions = PrivateOperations.getPaidPrivates(p);
        Map<String,String> birthdays = new HashMap<>();
        for(ProtectedRegion region : regions) {   try {birthdays.put(region.getId(), PaidRegionBirthdayDataBase.getString(region.getId()));}catch (Exception e){continue;}}

        /// ниже код от ИИ. мне пока что сложно разобраться с такой сортировкой
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        Map<String,String> sortedRegions = birthdays.entrySet()
                .stream().sorted(Comparator.comparing(entry ->{
                    String value = entry.getValue();
                    if (value == null) return LocalDate.MIN;

                    try {return LocalDate.parse(value, formatter);} catch (Exception e) {return LocalDate.MIN;}

                }))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
        /// --------------------------------------------------------------------
        return sortedRegions;
    }


    ///  форматирование многострочного сообщения Ps list
    private static List<String> formatPsListMessage(Player p){
        Map<String,String> mapRegionPlaceholders = getRgListPlaceholders(p);
        String placeholderRegionIDKey =         "%PaidPrivate%";
        String placeholderRegionNumberKey =     "%NumberOfPaidPrivate%";
        String placeholderRegionBirthdayKey =   "%BirthdayOfPaidPrivate%";
        String placeholderPageNextButtonKey =   "%PageNextButton%";
        String placeholderPagePreviousKey =     "%PagePreviousButton%";
        String placeholderRegionInfoButtonKey = "%PaidPrivateInfoButton%";

        ///  список сообщений Ps list.
        List<String> psListMessages = new ArrayList<>();
        psListMessages.add(message.messPsListHeader);
        psListMessages.add(message.messPsListBody);
        psListMessages.add(message.messPsListEnd);

        List<String> formatMessList = new ArrayList<>();

        int regionIterator = 0;
        boolean iteration = false;


        ///  перебор сообщений ps list
        for(String mess : psListMessages)
        {
            ///  перебор плейсхолдеров ps list
        for(Map.Entry<String,String> entry : mapRegionPlaceholders.entrySet())
        {
            /// mapRegionPlaceholders <String RegionID,String Birthday>
            String numberOfRegion = new String(""+regionIterator);
            String keyRegID = entry.getKey();
            String valueBirthday = entry.getValue();
            iteration = false;
            if(mess.contains(placeholderRegionIDKey)) { mess = mess.replace(placeholderRegionIDKey,keyRegID); iteration = true; }
            if(mess.contains(placeholderRegionBirthdayKey)) { mess = mess.replace(placeholderRegionBirthdayKey,valueBirthday); iteration =true;}
            if(mess.contains(placeholderRegionNumberKey)) {mess = mess.replace(placeholderRegionNumberKey,numberOfRegion); iteration = true;}

            if(iteration){regionIterator++;}
        }


        Map<String,String> placeHolders = GetPlaceHolders(p);

        /// перебор стандартных плейсхолдеров
        for(Map.Entry<String,String> entry : placeHolders.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();

            if(mess.contains(key)) {mess = mess.replace(key,value);}

        }

        String formatMess = ChatColor.translateAlternateColorCodes('&', mess);
        formatMessList.add(formatMess);

        }

        return formatMessList;
    }

    ///  форматирует заготовленные сообщения в формат сообщений для чата игры (цвет, стиль)
    private static String formatMessage(String mess, Player p)
    {

        Map<String,String> placeHolders = GetPlaceHolders(p);


        for(Map.Entry<String,String> entry : placeHolders.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();

            if(mess.contains(key)) {mess = mess.replace(key,value);}

        }

            return ChatColor.translateAlternateColorCodes('&', mess);
    }


    ///  Компановщик сообщений ps list. Преоброзует список строк в одно сообщение с переходами на новую строку. вызывать перед отправкой сообщения игроку
    private static String CompactPsListMessages(List<String> list)
    {
        String compactMess = "";
        for(String  message : list){ compactMess = compactMess + message + "\n"; }
        return compactMess;
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

    public static void PsListMessages(Player p) { p.sendMessage(CompactPsListMessages(formatPsListMessage(p))); }
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
        String messPsListHeader;
        String messPsListBody;
        String messPsListEnd;
        String messPsListPageButtons;

        String styleButtonPageNext;
        String styleButtonPagePrevious;
        String styleButtonInfo;

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



            messPsListHeader = messHeader + config.getString("MessPsListHeader");
            messPsListBody = config.getString("MessPsListBody");
            messPsListEnd = config.getString("MessPsListEnd");
            messPsListPageButtons = config.getString("MessPsListPageButtons");

            styleButtonPageNext = config.getString("StyleButtonPageNext");
            styleButtonPagePrevious = config.getString("StyleButtonPagePrevious");
            styleButtonInfo = config.getString("StyleButtonInfo");


        }
    }
}

package PSPlugins.buyingRegions.Messages;
import PSPlugins.buyingRegions.CommandsImplementation.PrivateOperations;
import PSPlugins.buyingRegions.Files.GetOptionsConfig;
import PSPlugins.buyingRegions.Files.MessagesConfig;
import PSPlugins.buyingRegions.CommandsImplementation.Cost;
import PSPlugins.buyingRegions.CommandsImplementation.CostDataBox;

import PSPlugins.buyingRegions.Files.PaidRegionBirthdayDataBase;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.md_5.bungee.api.chat.ClickEvent;

import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


import java.util.*;


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


        PlaceHolders.put("%MAX_REGION_SIZE%",String.valueOf(new GetOptionsConfig().region_volume_max));
        PlaceHolders.put("%MIN_REGION_SIZE%",String.valueOf(new GetOptionsConfig().region_volume_min));
        PlaceHolders.put("%MAX_SUBREGION_SIZE%", String.valueOf(new GetOptionsConfig().subregion_volume_max));
        PlaceHolders.put("%MIN_SUBREGION_SIZE%", String.valueOf(new GetOptionsConfig().subregion_volume_min));


        if(p.hasMetadata(mDataPrivateName))
        {   var name = p.getMetadata(mDataPrivateName).get(0).asString();
            PlaceHolders.put("%REGION_NANE%", name);}


        if(data != null) {
            PlaceHolders.put("%X_SIZE%", String.valueOf((int)data.size.getX()));
            PlaceHolders.put("%Y_SIZE%", String.valueOf((int)data.size.getY()));
            PlaceHolders.put("%Z_SIZE%", String.valueOf((int)data.size.getZ()));
            PlaceHolders.put("%SUMM_SIZE%",String.valueOf(data.summSize));


            if(data.priceSubPrivate < 0 || data.priceSubPrivate >= 2147483647) {PlaceHolders.put("%PRICE_SUB%","2147483647+"); } else {
            PlaceHolders.put("%PRICE_SUB%",String.valueOf(data.priceSubPrivate));}

            if(data.price < 0 || data.price >= 2147483647) {PlaceHolders.put("%PRICE%","2147483647+"); } else {
            PlaceHolders.put("%PRICE%",String.valueOf(data.price));}
        }



        return PlaceHolders;
    }

    ///  получаем название региона из текст компонента, если он там есть. иначе null
    private static String parseRegionIDFromTextComponent(TextComponent component, Player p)
    {

        Map<String,String> regionsAndBirthdays = getRgListPlaceholders(p);
        String parse = null;

        for(Map.Entry<String, String> regions : regionsAndBirthdays.entrySet())
        {
            if(component.getText().contains(regions.getKey()))
            {
                parse = regions.getKey();
            }
        }


        return parse;
    }

    /// добавление функционала кнопок
    private static TextComponent buttonsFormater(TextComponent messTextComp, int currentPage, Player p) {
        String mess = messTextComp.getText();
        mess = spaceFormatButton(formatMessage(mess, p));

        try {
            TextComponent component = new TextComponent();
            String[] words = mess.split("[$#]");
            String regionID = null;
            boolean thisIsButton = false;

            for (String word : words) {
                thisIsButton=false;

                ///  взятие названия региона для команды кнопки
                if(word != null && !word.isEmpty() && parseRegionIDFromTextComponent(new TextComponent(word),p) != null) {
                regionID = parseRegionIDFromTextComponent(new TextComponent(word), p);}

                /// создание кнопки rg info regionID
                if (word.contains(message.styleButtonInfo)){

                    TextComponent button = new TextComponent(formatMessage(word, p));
                    button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/rg info " + regionID));
                    button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new net.md_5.bungee.api.chat.hover.content.Text(formatMessage(message.hoverMessButtonInfo,p))));

                    component.addExtra(new TextComponent(" "));
                    component.addExtra(button);
                    component.addExtra(new TextComponent(" "));
                    thisIsButton = true;
                } else  {

                }

                /// создание кнопки /ps list N+1
                if(word.contains(message.styleButtonPageNext))
                {

                    int nextPage = currentPage+2;

                    TextComponent button = new TextComponent(formatMessage(word, p));
                    button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ps list " + nextPage));
                    button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new net.md_5.bungee.api.chat.hover.content.Text(formatMessage(message.hoverMessButtonPageNext,p))));
                    component.addExtra(new TextComponent(" "));
                    component.addExtra(button);
                    component.addExtra(new TextComponent(" "));
                    thisIsButton = true;
                }
                /// создание кнопки /ps list N-1
                if(word.contains(message.styleButtonPagePrevious))
                {

                    int previousPage = currentPage;
                    TextComponent button = new TextComponent(formatMessage(word, p));
                    button.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ps list " + previousPage));
                    button.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new net.md_5.bungee.api.chat.hover.content.Text(formatMessage(message.hoverMessButtonPagePrevious,p))));
                    component.addExtra(new TextComponent(" "));
                    component.addExtra(button);
                    component.addExtra(new TextComponent(" "));
                    thisIsButton = true;
                }
                if(!thisIsButton){component.addExtra(new TextComponent(formatMessage(word, p)));}
            }



            return component;
        } catch (Exception e) {
            System.out.println("Exc ButtonFormater : " + e);
            return null;
        }
    }



    /// сбор всех имен платных регионов и их даты создания.
    private static Map<String,String> getRgListPlaceholders(Player p) {
        ///  сбор значений для Ps List
        try {


            Collection<ProtectedRegion> regions = PrivateOperations.getPaidPrivates(p);
            Map<String, String> birthdays = new HashMap<>();
            for (ProtectedRegion region : regions) {
                try {
                    String pathBirthday = PaidRegionBirthdayDataBase.getPath(p.getName(),region.getId(),PrivateOperations.getWorld(region).getName());
                    if(PaidRegionBirthdayDataBase.getString(pathBirthday) == null || PaidRegionBirthdayDataBase.getString(pathBirthday).isEmpty())
                    {birthdays.put(region.getId(),"Null");} else {
                    birthdays.put(region.getId(), PaidRegionBirthdayDataBase.getString(pathBirthday));}

                } catch (Exception e) {System.out.println("Exc Birthday null:" + e); birthdays.put(region.getId(),"Null");}
            }
            return birthdays;

        } catch (Exception e) {System.out.println("Exc " + e);   return null;}
    }

    /// получение готовой страницы сообщения
    private static TextComponent getPsListPageMess(Player p, int numberOfPage)
    {
        try {
            List<TextComponent> messages = new ArrayList<>();
            messages.add(new TextComponent(formatMessage(message.messPsListHeader,p)));
            messages.add(new TextComponent(getPsListPages(p).get(numberOfPage)));

            ///  получаем кол-во страниц для определения psList end
            int number_regions = PrivateOperations.getPaidPrivates(p).size();
           int pages = (int) Math.ceil((double) new GetOptionsConfig().number_regions_page / (double)PrivateOperations.getPaidPrivates(p).size());
           if(pages==0 || number_regions == new GetOptionsConfig().number_regions_page){  messages.add(new TextComponent(formatMessage(message.messPsListEnd,p))); }else
           {messages.add(new TextComponent(buttonsFormater(new TextComponent(message.messPsListPageButtons),numberOfPage,p)));}

            return  compactMessages(messages);
        }catch (Exception e){System.out.println("Exc getPsListPageMess : " +e); return null;}



    }

    ///  форматирование многострочного сообщения Ps list
    private static List<TextComponent> getPsListPages(Player p){
        try {

            int number_regions = PrivateOperations.getPaidPrivates(p).size();
            int number_regions_per_page = new GetOptionsConfig().number_regions_page;
            

            ///  собранные  страницы.
            List<TextComponent> pages_mess = new ArrayList<>();

            /// кэшевый сборщик страниц
            List<TextComponent> cash_page_mess = new ArrayList<>();

            /// список значений для плейсхолдеров ps list
         Map<String,String> regionsAndBirthdays = getRgListPlaceholders(p);

        String placeholderRegionIDKey =         "%PaidPrivate%";
        String placeholderRegionNumberKey =     "%NumberOfPaidPrivate%";
        String placeholderRegionBirthdayKey =   "%BirthdayOfPaidPrivate%";

        ///  список сообщений Ps list.
        String body   =  message.messPsListBody;


        /// готовые сообщения
        List<TextComponent> formatMessList = new ArrayList<>();

          ///  formatMessList.add(formatMessage(header,p));

        int regionIterator = 1;
            Map<String,String> placeHolders = GetPlaceHolders(p);

            ///  перебор плейсхолдеров ps list
            if (regionsAndBirthdays != null && !regionsAndBirthdays.isEmpty()) {
         for(Map.Entry<String,String> entry : regionsAndBirthdays.entrySet())
          {

              /// перебор стандартных плейсхолдеров
              for(Map.Entry<String,String> i : placeHolders.entrySet())
              {
                  String key = i.getKey();
                  String value = i.getValue();

                  if(body.contains(key)) {body = body.replace(key,value);}

              }
              String cashBodyMess = body;

            /// mapRegionPlaceholders <String RegionID,String Birthday>
            String numberOfRegion = new String(""+regionIterator);
            String valueBirthday = entry.getValue();

            String keyRegID = entry.getKey();


              /// замена плейсхолдеров
            if(cashBodyMess.contains(placeholderRegionIDKey)) { cashBodyMess = cashBodyMess.replace(placeholderRegionIDKey,keyRegID);}
            if(cashBodyMess.contains(placeholderRegionBirthdayKey)) { cashBodyMess = cashBodyMess.replace(placeholderRegionBirthdayKey,valueBirthday); }
            if(cashBodyMess.contains(placeholderRegionNumberKey)) {cashBodyMess=  cashBodyMess.replace(placeholderRegionNumberKey,numberOfRegion);}



            ///  смещение итератора
              regionIterator++;

              ///  строка с цветом и заменой плейсхолдеров  
              String formatMess = ChatColor.translateAlternateColorCodes('&', cashBodyMess);

              ///  преобразование строки в кнопку
               TextComponent formatted = buttonsFormater(new TextComponent(formatMess), -1, p);
              /// добавление строки в список всех регионов и их строк (требуется разделение на страницы)
              formatMessList.add(formatted);}

            } else { System.out.println(" mapRegionPlaceholders Exc "); }

            ///  итератор-костыль. для определения начала страницы.
            int pages_iter = 1;
            /// Сборщик страниц
            for (int i = 1; i <= number_regions; i++)
            {
                /// сбор строк в "Страницу без индекса"


                cash_page_mess.add(formatMessList.get(i-1));

                ///  разделитель страниц
                if(i == number_regions_per_page*pages_iter || i == number_regions)
                {
                    ///  добавление страницы в список страниц
                    pages_mess.add(compactMessages(cash_page_mess));
                    ///  очистка данной страницы. что бы исключить дублирование страниц
                    cash_page_mess = new ArrayList<>();
                    pages_iter++;

                }


            }

        return pages_mess;
        } catch (Exception e) { System.out.println("Exc getPsListPageMessage :" + e);return null; }
    }

    /// метод для форматирования кнопок. добавляет коды для работы кнопок. добавляет стиль кнопок. но не красит их в цвет.
    private  static String spaceFormatButton(String button)
    {
        if(button.contains("%PaidPrivateInfoButton%")){ button = button.replace("%PaidPrivateInfoButton%","$#"+message.styleButtonInfo+"$#"); }
        else if(button.contains(message.styleButtonInfo)){button = button.replace(message.styleButtonInfo, "$#"+message.styleButtonInfo+"$#");}

        if(button.contains("%PagePreviousButton%")) {button = button.replace("%PagePreviousButton%","$#"+message.styleButtonPagePrevious+"$#");}
        else if(button.contains(message.styleButtonPagePrevious)){button = button.replace(message.styleButtonPagePrevious, "$#"+message.styleButtonPagePrevious+"$#");}

        if(button.contains("%PageNextButton%")){button = button.replace("%PageNextButton%","$#"+message.styleButtonPageNext+"$#");}
        else if(button.contains(message.styleButtonPageNext)){button = button.replace(message.styleButtonPageNext, "$#"+message.styleButtonPageNext+"$#");}

        return button;
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


    ///  Компановщик сообщений  Преоброзует список строк в одно сообщение с переходами на новую строку. вызывать перед отправкой сообщения игроку
    private static TextComponent compactMessages(List<TextComponent> list)
    {

        TextComponent compactMess = new TextComponent();
        for (TextComponent message : list) {
            compactMess.addExtra(new TextComponent(""));
            compactMess.addExtra(message);
            compactMess.addExtra(new TextComponent(""));

            if(message != list.getLast()){
            compactMess.addExtra(new TextComponent("\n"));}
        }

        return compactMess;
    }

    ///  компановшик сообщений на базе String. Преоброзует список строк в одно сообщение с переходами на новую строку. вызывать перед отправкой сообщения игроку
    private static  String compactMessagesString(List<String> list)
    {

        StringBuilder compactMess = new StringBuilder();
        for (String message : list) {

            compactMess.append(message);

            if(!message.equals(list.getLast())) {
                compactMess.append("\n");}
        }
        return String.valueOf(compactMess);
    }

    ///  ошибка. выделение больше лимита блоков
    public  static  void ErrorLimitOfBlocks(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorLimitOfBlocks,p));
    }

    public  static void ErrorMinLimitOfBlock(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorMinimalLimitOfBlock,p));
    }

    public  static void ErrorSubLimitOfBlock(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorSubLimitOfBlocks,p));
    }

    public static void ErrorMinimalSubLimitOfBlock(Player p)
    {
        p.sendMessage(formatMessage(message.messErrorMinimalSuBLimitOfBlock,p));
    }


    ///  сообщения подсчета выделения
    public static void CostMess(Player p, int summSize)
    {
        List<String> messages = new ArrayList<>();
        GetOptionsConfig optionsConfig = new GetOptionsConfig();
        if(summSize > optionsConfig.region_volume_max || summSize < optionsConfig.region_volume_min)
        {ErrorLimitOfBlocks(p);}

        messages.add(formatMessage(message.messSummRegionSize,p));
        messages.add(formatMessage(message.messSizeRegionXYZ,p));
        messages.add(formatMessage(message.messPrice,p));

        p.sendMessage(compactMessagesString(messages));

    }

    ///  сообщения подсчета выделения без цены
    public  static void SizeMess(Player p, int summSize)
    {
        List<String> messages = new ArrayList<>();

        GetOptionsConfig optionsConfig = new GetOptionsConfig();
        if(summSize > optionsConfig.region_volume_max || summSize < optionsConfig.region_volume_min)
        {messages.add(formatMessage(message.messErrorLimitOfBlocks,p));}

        messages.add(formatMessage(message.messSummRegionSize,p));

        messages.add((formatMessage(message.messSizeRegionXYZ,p)));
        p.sendMessage(compactMessagesString(messages));
    }

    public static void PsListMessages(Player p,int numberOfPage)
    {
        if(PrivateOperations.getPaidPrivates(p) == null || PrivateOperations.getPaidPrivates(p).isEmpty())
        {p.sendMessage(formatMessage(message.messPsListNotFoundRegions,p));}
        else
            p.spigot().sendMessage(getPsListPageMess(p,numberOfPage));
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
        String messErrorName;
        String messErrorNotFoundParent;
        String messErrorNameRegEx;
        String messPsListHeader;
        String messPsListBody;
        String messPsListEnd;
        String messPsListPageButtons;
        String messPsListNotFoundRegions;
        String messErrorLimitOfBlocks;
        String messErrorSubLimitOfBlocks;
        String messErrorMinimalLimitOfBlock;
        String messErrorMinimalSuBLimitOfBlock;

        String styleButtonPageNext;
        String styleButtonPagePrevious;
        String styleButtonInfo;

        String hoverMessButtonPageNext;
        String hoverMessButtonPagePrevious;
        String hoverMessButtonInfo;

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
           messErrorName = messHeader + config.getString("MessErrorName");
           messErrorNotFoundParent = messHeader + config.getString("MessErrorNotFoundParent");
           messErrorNotFoundNameSub = messHeader + config.getString("MessErrorNotFoundNameSub");
           messErrorNameRegEx = messHeader + config.getString("MessErrorNameRegEx");
           messErrorLimitOfBlocks = messHeader + config.getString("MessErrorLimitOfBlocks");
           messErrorSubLimitOfBlocks = messHeader + config.getString("MessErrorSubLimitOfBlocks");
           messErrorMinimalLimitOfBlock = messHeader + config.getString("MessErrorMinimalLimitOfBlock");
           messErrorMinimalSuBLimitOfBlock = messHeader + config.getString("MessErrorMinimalSuBLimitOfBlock");



            messPsListNotFoundRegions = messHeader + config.getString("MessPsListNotFoundRegions");
            messPsListHeader = messHeader + config.getString("MessPsListHeader");
            messPsListBody =  config.getString("MessPsListBody");
            messPsListEnd = messHeader + config.getString("MessPsListEnd");
            messPsListPageButtons = messHeader + config.getString("MessPsListPageButtons");

            styleButtonPageNext = config.getString("StyleButtonPageNext");
            styleButtonPagePrevious = config.getString("StyleButtonPagePrevious");
            styleButtonInfo = config.getString("StyleButtonInfo");


            hoverMessButtonPageNext = config.getString("HoverMessButtonPageNext");
            hoverMessButtonPagePrevious = config.getString("HoverMessButtonPagePrevious") ;
            hoverMessButtonInfo = config.getString("HoverMessButtonInfo");



        }
    }
}

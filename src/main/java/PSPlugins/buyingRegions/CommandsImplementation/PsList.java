package PSPlugins.buyingRegions.CommandsImplementation;

import PSPlugins.buyingRegions.Files.GetOptionsConfig;
import PSPlugins.buyingRegions.Messages.psMessages;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PsList {



    public static void showList(Player p, String numberOfPageStr)
    {
        int number_regions = PrivateOperations.getPaidPrivates(p).size();
        int number_regions_per_page = new GetOptionsConfig().number_regions_page;

        ///  получаем кол во страниц
        int pages = (int)Math.ceil(number_regions / number_regions_per_page);

        try {
            int numberOfPage = 0;
            Pattern pattern = Pattern.compile("\\d");
            Matcher matcher = pattern.matcher(numberOfPageStr);
            if(matcher.find())
            {numberOfPage = Integer.parseInt(matcher.group());}
            if(numberOfPage>pages){numberOfPage = pages;} else if(numberOfPage <= 0) {numberOfPage = 0;} else {numberOfPage = numberOfPage-1;}
            psMessages.PsListMessages(p,numberOfPage);

        }catch (Exception e){System.out.println("Exc ShowList : "+ e);}
    }

    public static void showList(Player p)
    {
        try {
            psMessages.PsListMessages(p,0);
        }catch (Exception e){System.out.println("Exc ShowList : "+ e);}

        try {
            psMessages.SendTesButtonMessage(p);
        }catch (Exception e){System.out.println("Exc TestButtonMess ="+ e);}
    }


}

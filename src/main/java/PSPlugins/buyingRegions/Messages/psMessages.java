package PSPlugins.buyingRegions.Messages;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import  org.bukkit.util.Vector;

public class psMessages {

        static String PSStr = ChatColor.translateAlternateColorCodes('&', "&8&l[&6&lPS&8&l] ");


    public static void CostMess(Player p, int price, Vector size, int summSize)
    {
        if(summSize > 20000000)
        {
            p.sendMessage( PSStr + ChatColor.RED + "Размер региона превышает лимит в 20.000.000 блоков. вы НЕ сможете приобрести такой регион!!!");
        }

        p.sendMessage(PSStr
                + ChatColor.YELLOW + "Размер региона " + ChatColor.RED +  "" + ChatColor.BOLD +"X"
                + ChatColor.GREEN + "" + ChatColor.BOLD + "Y"
                + ChatColor.BLUE + "" + ChatColor.BOLD + "Z"
                + ChatColor.GREEN + " : " + (int)size.getX() + " " + (int)size.getY() + " " + (int)size.getZ());
        p.sendMessage(PSStr + ChatColor.YELLOW + "Суммарное кол-во. блоков составляет : " + ChatColor.GREEN + summSize);
        p.sendMessage(PSStr + ChatColor.YELLOW + "Стоимость региона составляет : " + ChatColor.GREEN + "" + ChatColor.BOLD + price + ChatColor.YELLOW + "$");

    }

    public  static void SizeMess(Player p, Vector size, int summSize)
    {

        if(summSize > 20000000)
        {
            p.sendMessage(PSStr + ChatColor.RED + "Размер региона превышает лимит в 20.000.000 блоков. вы НЕ сможете приобрести такой регион!!!");
        }

        p.sendMessage(PSStr + ChatColor.YELLOW + "Суммарное кол-во. блоков составляет : " + ChatColor.GREEN + summSize);

        p.sendMessage(PSStr
                + ChatColor.YELLOW + "Размер региона " + ChatColor.RED +  "" + ChatColor.BOLD +"X"
                + ChatColor.GREEN + "" + ChatColor.BOLD + "Y"
                + ChatColor.BLUE + "" + ChatColor.BOLD + "Z"
                + ChatColor.GREEN + " : " + (int)size.getX() + " " + (int)size.getY() + " " + (int)size.getZ());


    }

    public  static void NotFoundSelectionMess(Player p)
    {   p.sendMessage(PSStr
            + ChatColor.RED + "Выделение не найдено! выделите две точки //pos1 и //pos2");
    }

    public static void NotFoundNamePrivateMess(Player p)
    {
        p.sendMessage(PSStr +
                ChatColor.RED + "Укажите название региона!" + ChatColor.YELLOW + "   /ps rgbuy [название]" );
    }

    public static void PrivateNameErrorMess(Player p, String privateName)
    {
        p.sendMessage(PSStr +
                ChatColor.RED + "Название "+ ChatColor.GREEN + ChatColor.BOLD + privateName + ChatColor.RED +" уже занято! выберите другое название региона.");

    }

    public static void PrivatAreaErrorMess(Player p)
    {
        p.sendMessage(PSStr +
                ChatColor.RED + "Здесь уже есть регион! выберите другое место для региона.");

    }
}

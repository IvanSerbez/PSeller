package PSPlugins.buyingRegions.Messages;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import  org.bukkit.util.Vector;

public class psMessages {




    public static void CostMess(Player p, int price, Vector size, int summSize)
    {
        if(summSize > 20000000)
        {
            p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD+ "PrivateSeller" + ChatColor.GREEN + ": " + ChatColor.RED + "Размер региона превышает лимит в 20.000.000 блоков. вы НЕ сможете приобрести такой регион!!!");
        }

        p.sendMessage(ChatColor.YELLOW  + "" + ChatColor.BOLD + "PrivateSeller" + ChatColor.GREEN + ": "
                + ChatColor.YELLOW + " Размер региона " + ChatColor.RED +  "" + ChatColor.BOLD +"X"
                + ChatColor.GREEN + "" + ChatColor.BOLD + "Y"
                + ChatColor.BLUE + "" + ChatColor.BOLD + "Z"
                + ChatColor.YELLOW + " : " + (int)size.getX() + " " + (int)size.getY() + " " + (int)size.getZ());
        p.sendMessage(ChatColor.YELLOW  + "" + ChatColor.BOLD + "PrivateSeller" + ChatColor.GREEN + ": " + ChatColor.YELLOW + " Суммарное кол-во. блоков составляет : " + ChatColor.GREEN + summSize);
        p.sendMessage(ChatColor.YELLOW  + "" + ChatColor.BOLD + "PrivateSeller" + ChatColor.GREEN + ": " + ChatColor.YELLOW + " Стоимость региона составляет : " + ChatColor.GREEN + "" + ChatColor.BOLD + price + ChatColor.YELLOW + "$");

    }

    public  static void SizeMess(Player p, Vector size, int summSize)
    {

        if(summSize > 20000000)
        {
            p.sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD+ "PrivateSeller" + ChatColor.GREEN + ": " + ChatColor.RED + "Размер региона превышает лимит в 20.000.000 блоков. вы НЕ сможете приобрести такой регион!!!");
        }

        p.sendMessage(ChatColor.YELLOW  + "" + ChatColor.BOLD + "PrivateSeller" + ChatColor.GREEN + ": " + ChatColor.YELLOW + " Суммарное кол-во. блоков составляет : " + ChatColor.GREEN + summSize);

        p.sendMessage(ChatColor.YELLOW  + "" + ChatColor.BOLD + "PrivateSeller" + ChatColor.GREEN + ": "
                + ChatColor.YELLOW + " Размер региона " + ChatColor.RED +  "" + ChatColor.BOLD +"X"
                + ChatColor.GREEN + "" + ChatColor.BOLD + "Y"
                + ChatColor.BLUE + "" + ChatColor.BOLD + "Z"
                + ChatColor.YELLOW + " : " + (int)size.getX() + " " + (int)size.getY() + " " + (int)size.getZ());


    }


}

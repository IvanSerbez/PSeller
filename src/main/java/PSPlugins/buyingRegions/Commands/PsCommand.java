package PSPlugins.buyingRegions.Commands;

import PSPlugins.buyingRegions.BuyingRegions;
import PSPlugins.buyingRegions.PrivateOperations.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PsCommand implements CommandExecutor, TabCompleter {


    final List<String> subCommands = Arrays.asList("confirm","cost","rgbuy","rgsub","size");

    BuyingRegions plugin;

    public PsCommand(BuyingRegions plugin)
    {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player p)
        {
            if(args.length == 0) return false;


            switch (args[0]) {
                case "confirm":  Confirm.confrimBuying(p);    break;
                case "cost":     Cost.costRegion(p);          break;
                case "rgbuy":    rgBuy.buyRegion(p);          break;
                case "rgsub":    rgSub.buySubPrivate(p);      break;
                case "size":     size.checkSizeRegion(p);     break;
            }

            return  true;

        }





        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {

        if(commandSender instanceof Player && args.length == 1){

            List<String> subComm = new ArrayList<>();

            for(String sub : subCommands) {

                if(sub.startsWith(args[0].toLowerCase())){
                   subComm.add(sub);
                }

            }
            return subComm;


        }

        return List.of();
    }
}

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

    public BuyingRegions plugin;

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
                case "confirm":  Confirm.confirmBuying(p,plugin);     break;
                case "cost":     Cost.costRegion(p);                  break;
                case "rgbuy":    if(args.length ==2 ){  rgBuy.buyRegion(p,args[1].toString(), plugin); } else { rgBuy.buyRegion(p); }   break;
                case "rgsub":    if(args.length ==2 ){  rgSub.buySubPrivate(p,args[1].toString(), plugin); } else { rgSub.buySubPrivate(p); }   break;
                case "size":     Cost.sizeRegion(p);                   break;
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

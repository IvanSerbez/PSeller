package PSPlugins.buyingRegions;

import PSPlugins.buyingRegions.Commands.PsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuyingRegions extends JavaPlugin {

    PsCommand pscomm;
    @Override
    public void onEnable() {

        pscomm = new PsCommand(this);
        getCommand("ps").setExecutor(pscomm);
        getCommand("ps").setTabCompleter(pscomm);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

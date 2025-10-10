package PSPlugins.buyingRegions;

import PSPlugins.buyingRegions.Commands.PsCommand;
import PSPlugins.buyingRegions.Hooks.VaultHook;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuyingRegions extends JavaPlugin {

    PsCommand pscomm;
    @Override
    public void onEnable() {

        pscomm = new PsCommand(this);
        getCommand("ps").setExecutor(pscomm);
        getCommand("ps").setTabCompleter(pscomm);

        VaultHook.setupEconomy(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

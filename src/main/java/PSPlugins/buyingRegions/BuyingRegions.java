package PSPlugins.buyingRegions;

import PSPlugins.buyingRegions.Commands.PsCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuyingRegions extends JavaPlugin {

    @Override
    public void onEnable() {

        getCommand("ps").setExecutor(new PsCommand(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

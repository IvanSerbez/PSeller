package PSPlugins.buyingRegions;

import PSPlugins.buyingRegions.Commands.PsCommand;
import PSPlugins.buyingRegions.Hooks.VaultHook;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.BooleanFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class BuyingRegions extends JavaPlugin {

    PsCommand pscomm;
    public static final BooleanFlag PAID_FLAG = new BooleanFlag("paid");
    @Override
    public void onLoad() {
        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
        try {
            registry.register(PAID_FLAG);
        }catch (Exception e)
        {/*Error флаг занят другим плагином!*/}
    }

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

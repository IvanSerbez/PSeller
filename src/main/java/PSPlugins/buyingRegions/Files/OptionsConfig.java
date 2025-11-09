package PSPlugins.buyingRegions.Files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class OptionsConfig {



    private static File file;
    private  static FileConfiguration optionsConfig;

    public static void setup() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BuyingRegions");
        file = new File(plugin.getDataFolder(), "Options.yml");

        if (!file.exists()) {
            plugin.saveResource("Options.yml", false);
        }

        optionsConfig = YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration get(){return optionsConfig;}

    public static void save() {
        try {
            optionsConfig.save(file);

        } catch (IOException e) {
        }
    }
    public static void reload()
    {
        optionsConfig = YamlConfiguration.loadConfiguration(file);
    }


}

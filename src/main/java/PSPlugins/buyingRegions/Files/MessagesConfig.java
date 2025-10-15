package PSPlugins.buyingRegions.Files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class MessagesConfig {


    private static File file;
    private  static FileConfiguration messagesConfig;

    public static void setup() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BuyingRegions");
        file = new File(plugin.getDataFolder(), "Messages.yml");

        if (!file.exists()) {
            plugin.saveResource("Messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration get(){return messagesConfig;}

    public static void save() {
        try {
            messagesConfig.save(file);

        } catch (IOException e) {
        }
    }
    public static void reload()
    {
        messagesConfig = YamlConfiguration.loadConfiguration(file);
    }

}

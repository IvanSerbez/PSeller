package PSPlugins.buyingRegions.Files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class PaidRegionBirthdayDataBase {

    private static File file;
    private  static FileConfiguration paidRegionBirthdayDataBase;

    public static void setup() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("BuyingRegions");
        file = new File(plugin.getDataFolder(), "PaidRegionBirthdayDataBase.yml");

        if (!file.exists()) {
            plugin.saveResource("PaidRegionBirthdayDataBase.yml", false);
        }

        paidRegionBirthdayDataBase = YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration get(){return paidRegionBirthdayDataBase;}

    public static void save() {
        try {
            paidRegionBirthdayDataBase.save(file);

        } catch (IOException e) {
        }
    }
    public static void reload()
    {
        paidRegionBirthdayDataBase = YamlConfiguration.loadConfiguration(file);
    }

    public static void addString(String path,String key)
    {
        paidRegionBirthdayDataBase.set(path,key);
        save();
        reload();
    }
    public  static void  deleteString(String key)
    {
        try {paidRegionBirthdayDataBase.set(key,null);}catch (Exception e){}
    }
    public  static String getString(String key)
    {   String value = null;
        try {
            value = paidRegionBirthdayDataBase.getString(key);} catch (Exception e) {}
        return value;
    }

}

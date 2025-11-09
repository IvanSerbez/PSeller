package PSPlugins.buyingRegions.Files;

import org.bukkit.configuration.file.FileConfiguration;

public class GetOptionsConfig{


    private FileConfiguration config = OptionsConfig.get();

    public double start_cost, region_multiplier, region_ratio, volume_ratio ,subregion_cost;
    public int region_volume_max, region_volume_min,subregion_volume_max,subregion_volume_min;


    public GetOptionsConfig() {
        try {
            start_cost = config.getDouble("start_cost");
            region_multiplier = config.getDouble("region_multiplier");
            region_ratio = config.getDouble("region_ratio");
            volume_ratio = config.getDouble("volume_ratio");
            subregion_cost = config.getDouble("subregion_cost");

            region_volume_max = config.getInt("region_volume_max");
            region_volume_min = config.getInt("region_volume_min");
            subregion_volume_max = config.getInt("subregion_volume_max");
            subregion_volume_min = config.getInt("subregion_volume_min");


        } catch (Exception e) {
            System.out.println("OptionsConfig Поврежден! Exc: " + e);
        }
    }

}

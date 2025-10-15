package PSPlugins.buyingRegions.PrivateOperations;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CostDataBox
    {
       public Player player;
       public int price, summSize, priceSubPrivate;
       public Vector size;
        CostDataBox(Player p, int price, Vector size, int summSize, int priceSubPrivate)
        {
            this.player = p;
            this.price = price;
            this.summSize = summSize;
            this.size = size;
            this.priceSubPrivate = priceSubPrivate;

        }
    }

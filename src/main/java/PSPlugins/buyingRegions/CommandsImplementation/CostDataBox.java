package PSPlugins.buyingRegions.CommandsImplementation;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;


///  просто коробка для данных выделения
public class CostDataBox
    {
        ///  Игрок. который ввел команду
       public Player player;

       ///  стоимость обычного региона /// суммарное кол-во. блоков выделения /// стоимость суб региона
       public int price, summSize, priceSubPrivate, numberPrivates;

       ///  размер выделения XYZ
       public Vector size;

        CostDataBox(Player p, int price, Vector size, int summSize, int priceSubPrivate, int numberPrivates)
        {
            this.player = p;
            this.price = price;
            this.summSize = summSize;
            this.size = size;
            this.priceSubPrivate = priceSubPrivate;

        }
    }

package plugin.worldRegion.flags;

import cn.nukkit.Player;
import plugin.worldRegion.region.Region;

import java.util.List;

public class SetRegionFlag {
    public static void setFlag(Region region, Player player, Flag flag) {
        if (player.isOp() || region.owner == player.getUniqueId()) {
            region.flags = List.of(new Flag[]{flag});
        }
    }

    public static void setFlags(Region region, Player player, Flag[] flags) {
        if (player.isOp() || region.owner == player.getUniqueId()) {
            region.flags = List.of(flags);
        }
    }
}

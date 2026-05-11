package plugin.worldRegion.flags;

import cn.nukkit.Player;
import plugin.worldRegion.flags.reaction.SuccessfulFlag;
import plugin.worldRegion.region.Region;

public class CheckRegionFlag {
    public static void checkFlag(Region region, Player player) {
        SuccessfulFlag.execute(player, "flags: %" +region.flags.toString() + "%");
    }
}

package plugin.worldRegion.flags;

import cn.nukkit.Player;
import plugin.worldRegion.flags.reaction.SuccessfulFlag;
import plugin.worldRegion.flags.reaction.UnsuccessfulFlag;
import plugin.worldRegion.region.Region;

public class DeleteRegionFlag {
    public static void deleteFlag(Region region, Player player, Flag flag) {
        if (player.isOp() || region.owner == player.getUniqueId()) {
            if (!region.flags.contains(flag)) {
                UnsuccessfulFlag.execute(player, "flag is not");
                return;
            }
            region.flags.remove(flag);
            SuccessfulFlag.execute(player, "flag is delete!");
        }
    }

    public static void deleteFlags(Region region, Player player, Flag[] flags) {
        for (Flag flag : flags) {
            deleteFlag(region, player, flag);
        }
        return;
    }
}

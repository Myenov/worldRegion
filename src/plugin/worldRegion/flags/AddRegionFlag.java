package plugin.worldRegion.flags;

import cn.nukkit.Player;
import plugin.worldRegion.flags.reaction.SuccessfulFlag;
import plugin.worldRegion.flags.reaction.UnsuccessfulFlag;
import plugin.worldRegion.region.Region;


public class AddRegionFlag {
    public static void addFlag(Region region, Player player, Flag flag) {
        if (player.isOp()) {
            region.flags.add(flag);
            SuccessfulFlag.execute(player, "successful flag add " + flag.toString());
            return;
        }
        if (player.getUniqueId() == region.owner) {
            region.flags.add(flag);
            SuccessfulFlag.execute(player, "successful flag add " + flag.toString());
            return;
        }
        if (player.getUniqueId() != region.owner){
            UnsuccessfulFlag.execute(player, "unsuccessful flag add, you not owner");
            return;
        }
        UnsuccessfulFlag.execute(player, "unknown error");
    }

    public static void addFlag(Region region, Player player, Flag[] flags) {
        for (Flag flag : flags) {
            addFlag(region, player, flag);
        }
        return;
    }
}

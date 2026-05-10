package plugin.worldRegion.region;

import cn.nukkit.Player;
import cn.nukkit.level.Location;
import cn.nukkit.utils.TextFormat;
import plugin.worldRegion.flags.AllFlagList;

import java.util.Objects;
import java.util.UUID;

public class CreateRegion {
    public static void createRegion(Location locate, Player player, String name) {
        UUID playerUuid = player.getUniqueId();
        Location pos1 = StorageRegion.getPositionOneOnUUID(playerUuid);
        Location pos2 = StorageRegion.getPositionTwoOnUUID(playerUuid);
        if (pos1 == null || pos2 == null) {
            player.sendMessage(
                    TextFormat.RED + "" + TextFormat.BOLD +
                            "Сначала выберите две точки!");
        }
        if (player.isOp()) {
                StorageRegion.addRegion(
                        new Region(
                            name,
                                new int[]{(int) pos1.x, (int)pos1.y, (int)pos1.z},
                                new int[]{(int) pos2.x, (int)pos2.y, (int)pos2.z},
                                playerUuid, player.getName(), true,
                                new AllFlagList[]{AllFlagList.DropItems,
                                        AllFlagList.InputCommand, AllFlagList.Walk}, "world",
                                true

                        )
                );
        }
        if (CheckRegion.isRegion(locate)
                && Objects.requireNonNull(
                        CheckRegion.getRegion(locate)).owner == player.getUniqueId())
        {

        }
        if (!CheckRegion.isRegion(locate)) {

        }
        else {
            //return RegionError.PlaceOccupiedbyRegion;
        }
    }
}

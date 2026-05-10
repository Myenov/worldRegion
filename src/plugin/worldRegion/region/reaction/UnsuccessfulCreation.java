package plugin.worldRegion.region.reaction;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import plugin.worldRegion.utils.GetUserLanguage;

public class UnsuccessfulCreation {
    public static void execute(Player player, String placeholder) {
        player.sendMessage(
                TextFormat.GREEN + "" +
                        TextFormat.BOLD + GetUserLanguage.get(placeholder, player.getName())
        );
    }
}

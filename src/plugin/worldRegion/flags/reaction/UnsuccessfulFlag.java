package plugin.worldRegion.flags.reaction;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import plugin.worldRegion.utils.GetUserLanguage;

public class UnsuccessfulFlag {
    public static void execute(Player player, String placeholder) {
        player.sendMessage(
                TextFormat.RED + "" +
                        TextFormat.BOLD + GetUserLanguage.get(placeholder, player.getName())
        );
    }
}


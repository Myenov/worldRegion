package plugin.worldRegion.flags.reaction;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import plugin.worldRegion.utils.GetUserLanguage;

public class SuccessfulFlag {
    public static void execute(Player player, String placeholder) {
        player.sendMessage(
                TextFormat.GREEN + "" +
                        TextFormat.BOLD + GetUserLanguage.get(placeholder, player.getName())
        );
    }
}

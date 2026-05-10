package plugin.worldRegion.region.reaction;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import plugin.worldRegion.utils.GetUserLanguage;

public class SuccessfulCreation {
    public void execute(Player player) {
        player.sendMessage(
                TextFormat.GREEN + "" +
                TextFormat.BOLD + GetUserLanguage.get("SuccessfulCreation", player.getName())
        );
    }
}

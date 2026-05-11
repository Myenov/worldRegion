package plugin.worldRegion;

import cn.nukkit.plugin.PluginBase;

public class MainPlugin extends PluginBase{
    private static MainPlugin instance;
    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {}

    public static MainPlugin getInstance() {
        return instance;
    }

    public void onDisable() {}
}

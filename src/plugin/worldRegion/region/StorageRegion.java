package plugin.worldRegion.region;

import cn.nukkit.level.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class StorageRegion {
    private static final Map<UUID, Location> Position1 = new ConcurrentHashMap<>();
    private static final Map<UUID, Location> Position2 = new ConcurrentHashMap<>();
    public static final List<Region> Regions = new ArrayList<>();

    public static void addRegion(Region region) {
        Regions.add(region);
    }

    public static void removeRegion(Region region) {
        Regions.remove(region);
    }

    public static List<Region> getRegions() {
        return new ArrayList<>(Regions);
    }
}